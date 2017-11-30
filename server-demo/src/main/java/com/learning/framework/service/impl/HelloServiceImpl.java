package com.learning.framework.service.impl;

import com.learning.framework.server.RpcService;
import com.learning.framework.service.HelloService;
import org.springframework.stereotype.Service;

/**
 *
 * Created by fusj on 2017/11/29.
 */
@RpcService(HelloService.class)
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
