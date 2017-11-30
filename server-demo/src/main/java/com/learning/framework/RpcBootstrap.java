package com.learning.framework;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * Created by fusj on 2017/11/29.
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
