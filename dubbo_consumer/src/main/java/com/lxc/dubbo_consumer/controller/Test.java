package com.lxc.dubbo_consumer.controller;

import com.lxc.dubbo_consumer.service.TestHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Autowired
    private TestHelloService testHelloService;

    @GetMapping("/test")
    public void test(){
        testHelloService.test();
    }
}
