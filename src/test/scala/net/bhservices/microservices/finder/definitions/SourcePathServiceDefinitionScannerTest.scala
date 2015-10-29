package net.bhservices.microservices.finder.definitions

import java.io.File

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class SourcePathServiceDefinitionScannerTest extends FeatureSpec with GivenWhenThen with Matchers {

  info("As a User")
  info("I want to be able to find java source files")

  feature("Scanning single module Java project") {
    scenario("Ignore Pojo and Test classes") {
      Given("a Source path scanner")
      val provider = new SourcePathServiceDefinitionScannerProvider {
        override def sourcePath = new File("src/test/resources/test1/api/module-a-api")
      }
      val scanner = provider.scanner

      When("the source project directory is scanned")
      val result = scanner.scanDefinitions

      Then("the scanner should find all source file only")
      result should be(Seq(
        ServiceDefinition(name = "net.bhservices.module_a.ServiceA", module = "module-a-api")
      ))
    }

    scenario("Provide extends list when required") {
      Given("a Source path scanner")
      val provider = new SourcePathServiceDefinitionScannerProvider {
        override def sourcePath = new File("src/test/resources/test1/api/module-b-api")
      }
      val scanner = provider.scanner

      When("the source project directory is scanned")
      val result = scanner.scanDefinitions

      Then("the scanner should find all source file only")
      result should be(Seq(
        ServiceDefinition(name = "net.bhservices.module_b.BaseService", module = "module-b-api"),
        ServiceDefinition(name = "net.bhservices.module_b.ServiceB", module = "module-b-api", extendsList = Seq(
          "net.bhservices.module_b.BaseService",
          "net.bhservices.common.SomeCommonService"
        ))
      ))
    }
  }

  feature("Scanning multi module Java project") {
    scenario("Find all java source files") {
      Given("a Source path scanner")
      val provider = new SourcePathServiceDefinitionScannerProvider {
        override def sourcePath = new File("src/test/resources/test1")
      }
      val scanner = provider.scanner

      When("the source project directory is scanned")
      val result = scanner.scanDefinitions

      Then("the scanner should find all source file only")
      result should be(Seq(
        ServiceDefinition(name = "net.bhservices.module_a.ServiceA", module = "module-a-api"),
        ServiceDefinition(name = "net.bhservices.module_b.BaseService", module = "module-b-api"),
        ServiceDefinition(name = "net.bhservices.module_b.ServiceB", module = "module-b-api", extendsList = Seq(
          "net.bhservices.module_b.BaseService",
          "net.bhservices.common.SomeCommonService"
        )),
        ServiceDefinition(name = "net.bhservices.module_c.ServiceC", module = "module-c-api")
      ))
    }
  }
}
