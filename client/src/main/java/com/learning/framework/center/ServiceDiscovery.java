package com.learning.framework.center;

import com.learning.framework.client.ConnectManage;
import com.learning.framework.common.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Service Discovery
 * Created by fusj on 2017/11/29.
 */
public class ServiceDiscovery {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;

    private ZooKeeper zooKeeper;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
        this.zooKeeper = connectServer();
        if(zooKeeper != null) {
            watchNode(this.zooKeeper);
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        return zk;
    }

    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_ROOT_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_ROOT_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            LOG.debug("node data: {}", dataList);
            this.dataList = dataList;

            LOG.debug("Service discovery triggered updating connected server node.");
            UpdateConnectedServer();
        } catch (KeeperException | InterruptedException e) {
            LOG.error("", e);
        }
    }

    private void UpdateConnectedServer(){
        ConnectManage.getInstance().updateConnectedServer(this.dataList);
    }

    public void stop(){
        if(zooKeeper!=null){
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                LOG.error("", e);
            }
        }
    }

}
