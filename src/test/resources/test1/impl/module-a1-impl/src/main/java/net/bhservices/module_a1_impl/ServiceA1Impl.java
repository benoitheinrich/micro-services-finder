package net.bhservices.module_a1_impl;

import net.bhservices.module_a.ServiceA;
import net.bhservices.module_b.ServiceB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceA1Impl implements ServiceA {
    @Autowired
    private ServiceB serviceB;

    @Override
    public void someMethod() {

    }
}