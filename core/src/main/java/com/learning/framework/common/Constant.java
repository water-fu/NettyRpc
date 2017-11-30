package com.learning.framework.common;

/**
 * zookeeper constant
 * Created by fusj on 2017/11/24.
 */
public interface Constant {

    // zk connect timeout
    int ZK_SESSION_TIMEOUT = 5000;

    // retistry root path
    String ZK_REGISTRY_ROOT_PATH = "/registry";

    // registry data path
    String ZK_DATA_PATH = ZK_REGISTRY_ROOT_PATH + "/data";
}
