package net.bhservices.microservices.finder.cache

import java.io.{File, PrintWriter}

import net.bhservices.microservices.finder.definitions.ServiceDefinition
import net.bhservices.microservices.finder.marshaller.JsonMarshallerProvider
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

import scala.io.Source

class FileCacheProviderTest extends FeatureSpec with GivenWhenThen with Matchers {
  info("As a User")
  info("I want to be able to cache objects")

  feature("Using JSON as a marshaller") {
    scenario("Save sequence of objects to a temporary file") {
      val tempFile = mktemp
      Given(s"a temporary file is created as $tempFile")

      And("a JSON cache provider is created")
      val provider = new FileCacheProvider[Seq[ServiceDefinition]] with JsonMarshallerProvider[Seq[ServiceDefinition]] {
        override def cacheFile = tempFile
      }

      When("a service definition is cached")
      provider.cache.save(Seq(ServiceDefinition(name = "ClassA", module = "module-a")))
      val content = Source.fromFile(tempFile).mkString

      Then("the service definition should be saved as JSON in the temporary file")
      content shouldBe
        """
          |[
          |  {
          |    "name":"ClassA",
          |    "module":"module-a",
          |    "extendsList":[
          |      
          |    ]
          |  }
          |]""".stripMargin.trim
    }


    scenario("Read sequence of objects from a temporary file") {
      val tempFile = mktemp
      Given(s"a temporary file is created as $tempFile")

      And("a service definition is saved as JSON in the temporary file ")
      new PrintWriter(tempFile) {
        write( """[{"name":"ClassA","module":"module-a","extendsList":[]}]""")
        close()
      }

      And("a JSON marshaller for a single ServiceDefinition")
      val provider = new FileCacheProvider[Seq[ServiceDefinition]] with JsonMarshallerProvider[Seq[ServiceDefinition]] {
        override def cacheFile = tempFile
      }

      When("the cache is loaded")
      val result = provider.cache.load

      Then("it should load the service definition")
      result shouldBe Seq(ServiceDefinition(name = "ClassA", module = "module-a"))
    }
  }

  private def mktemp: File = {
    val tempFile = File.createTempFile("FileCacheProviderTest", "")
    tempFile.deleteOnExit()
    tempFile
  }
}
