package net.bhservices.module_c1_impl;

import net.bhservices.module_a.ServiceA;
import net.bhservices.module_b.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceC1Impl implements ServiceC {
    @Autowired
    private ServiceA serviceA;
    @Autowired
    private ServiceB serviceB;

    @Override
    public void someMethod() {

    }
}