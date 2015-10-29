package net.bhservices.module_b;

import net.bhservices.common.SomeCommonService;

public interface ServiceB extends BaseService, SomeCommonService {
    void someMethod();
}