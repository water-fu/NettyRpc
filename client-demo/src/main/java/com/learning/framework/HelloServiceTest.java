package com.learning.framework;

import com.learning.framework.client.RpcClient;
import com.learning.framework.service.HelloService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Created by fusj on 2017/11/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class HelloServiceTest {

    @Autowired
    private RpcClient rpcClient;

    @Test
    public void helloTest1() {
        HelloService helloService = rpcClient.create(HelloService.class);
        String result = helloService.sayHello("World");
        Assert.assertEquals("Hello World", result);
    }
}
