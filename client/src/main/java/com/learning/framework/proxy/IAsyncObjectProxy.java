package com.learning.framework.proxy;

import com.learning.framework.client.RPCFuture;

/**
 *
 * Created by fusj on 2017/11/29.
 */
public interface IAsyncObjectProxy {
    RPCFuture call(String funcName, Object... args);
}
