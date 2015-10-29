This test directory contains a multi-modules project composed of:
* 3 api modules:
  * module-a-api
  * module-b-api
  * module-c-api
* 4 implementation modules:
  * module-a1-impl
  * module-a2-impl
  * module-b1-impl
  * module-c1-impl

**module-a1-impl** contains `ServiceA1Impl` class which implements `ServiceA`,
and uses `ServicB` as a dependency.

**module-a2-impl** contains `ServiceA2Impl` class which implements `ServiceA`,
and uses `ServiceC` as a dependency.

**module-b1-impl** contains `ServiceB1Impl` class which implements `ServiceB`,
and doesn't use any other service.

**module-c1-impl** contains `ServiceC1Impl` class which implements `ServiceC`,
which uses `ServiceA` and `ServiceB`.
