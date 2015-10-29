package net.bhservices.module_a2_impl;

import net.bhservices.module_a.ServiceA;
import net.bhservices.module_c.ServiceC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceA2Impl implements ServiceA {
    @Autowired
    private ServiceC serviceC;

    @Override
    public void someMethod() {

    }
}