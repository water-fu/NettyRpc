package com.learning.framework.center;

import com.learning.framework.common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * service registry
 * Created by fusj on 2017/11/24.
 */
public class ServiceRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistry.class);

    // zk初始化等待器
    private CountDownLatch latch = new CountDownLatch(1);

    // 服务注册地址
    private String registryAddress;

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * service registry
     *
     * @param data
     */
    public boolean registry(String data) {
        if (data != null) {
            ZooKeeper zooKeeper = connectServer();
            if (zooKeeper != null) {
                addRootNode(zooKeeper);
                createNode(zooKeeper, data);
            }
        }

        return false;
    }

    /**
     * create data path, use sequence
     *
     * @param zooKeeper
     * @param data
     */
    private void createNode(ZooKeeper zooKeeper, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zooKeeper.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOG.debug("create zookeeper node: {} --> {}", path, data);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * create root path
     *
     * @param zooKeeper
     */
    private void addRootNode(ZooKeeper zooKeeper) {
        try {
            Stat stat = zooKeeper.exists(Constant.ZK_REGISTRY_ROOT_PATH, false);
            if (stat == null) {
                zooKeeper.create(Constant.ZK_REGISTRY_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * connect zookeeper
     *
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(this.registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return zk;
    }
}
