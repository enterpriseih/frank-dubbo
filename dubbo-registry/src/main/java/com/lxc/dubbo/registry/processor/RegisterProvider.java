package com.lxc.dubbo.registry.processor;

import com.lxc.dubbo.annotaion.FrankDubbo;
import com.lxc.dubbo.domain.Url;
import com.lxc.dubbo.register.LocalCache;
import com.lxc.dubbo.registry.Registry;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@Component
public class RegisterProvider implements BeanPostProcessor {

    @Autowired
    private Registry registry;

    @Value("${server.port}")
    private String port;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(FrankDubbo.class)) {
            Class<?>[] interfaces = beanClass.getInterfaces();
            Arrays.stream(interfaces).forEach(i -> {
                LocalCache.register(i.getName(), beanClass);
                String hostAddress = "";
                try {
                    hostAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }

                try {
                    registry.register(i.getName(), new Url(hostAddress, port));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return bean;
    }
}