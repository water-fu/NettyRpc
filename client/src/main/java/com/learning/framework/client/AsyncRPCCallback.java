package com.learning.framework.client;

/**
 * RPC Callback
 * Created by fusj on 2017/11/29.
 */
public interface AsyncRPCCallback {

    void success(Object result);

    void fail(Exception e);
}
