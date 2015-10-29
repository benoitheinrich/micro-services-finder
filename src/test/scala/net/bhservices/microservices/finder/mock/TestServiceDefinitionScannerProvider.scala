package net.bhservices.microservices.finder.mock

import net.bhservices.microservices.finder.definitions.{ServiceDefinition, ServiceDefinitionScannerProvider}

/**
 * A Simple implementation wich always return just one test service
 */
trait TestServiceDefinitionScannerProvider extends ServiceDefinitionScannerProvider {
  override def scanner = new TestServiceDefinitionScanner

  class TestServiceDefinitionScanner extends ServiceDefinitionScanner {
    override def scanDefinitions = Seq(ServiceDefinition(
      name = "test",
      module = "test.jar"
    ))
  }

}
