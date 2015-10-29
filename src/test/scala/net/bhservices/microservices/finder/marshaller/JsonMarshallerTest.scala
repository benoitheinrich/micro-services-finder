package net.bhservices.microservices.finder.marshaller

import net.bhservices.microservices.finder.definitions.ServiceDefinition
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class JsonMarshallerTest extends FeatureSpec with GivenWhenThen with Matchers {

  info("As a User")
  info("I want to be able to use JSON as a marshaller")

  feature("Using JSON as a marshaller") {
    scenario("Marshall a single object to JSON") {
      Given("a JSON marshaller for a single ServiceDefinition")
      val provider = new JsonMarshallerProvider[ServiceDefinition] {}
      val marshaller = provider.marshaller

      When("a service definition is marshalled")
      val result = marshaller.marshal(ServiceDefinition(name = "ClassA", module = "module-a", extendsList = Seq("ClassB")))

      Then("the result should be marshalled as JSON")
      result shouldBe
        """
          |{
          |  "name":"ClassA",
          |  "module":"module-a",
          |  "extendsList":[
          |    "ClassB"
          |  ]
          |}""".stripMargin.trim
    }

    scenario("Unmarshall a single object from JSON") {
      Given("a JSON marshaller for a single ServiceDefinition")
      val provider = new JsonMarshallerProvider[ServiceDefinition] {}
      val marshaller = provider.marshaller

      When("a service definition is unmarshalled")
      val result = marshaller.unmarshal(
        """
          |{
          |  "name":"ClassA",
          |  "module":"module-a",
          |  "extendsList":[
          |    "ClassB"
          |  ]
          |}""".stripMargin.trim)

      Then("the result should be unmarshalled as a service definition")
      result shouldBe ServiceDefinition(name = "ClassA", module = "module-a", extendsList = Seq("ClassB"))
    }

    scenario("Marshall a sequence of objects to JSON") {
      Given("a JSON marshaller for a sequence of ServiceDefinition")
      val provider = new JsonMarshallerProvider[Seq[ServiceDefinition]] {}
      val marshaller = provider.marshaller

      When("a service definition is marshalled")
      val result = marshaller.marshal(Seq(ServiceDefinition(name = "ClassA", module = "module-a", extendsList = Seq("ClassB"))))

      Then("the result should be marshalled as JSON")
      result shouldBe
        """
          |[
          |  {
          |    "name":"ClassA",
          |    "module":"module-a",
          |    "extendsList":[
          |      "ClassB"
          |    ]
          |  }
          |]""".stripMargin.trim
    }

    scenario("Unmarshall a sequence of objects from JSON") {
      Given("a JSON marshaller for a sequence of ServiceDefinition")
      val provider = new JsonMarshallerProvider[Seq[ServiceDefinition]] {}
      val marshaller = provider.marshaller

      When("a service definition is unmarshalled")
      val result = marshaller.unmarshal(
        """
          |[
          |  {
          |    "name":"ClassA",
          |    "module":"module-a",
          |    "extendsList":[
          |      "ClassB"
          |    ]
          |  }
          |]""".stripMargin.trim
      )

      Then("the result should be unmarshalled as a sequence of service definitions")
      result shouldBe Seq(ServiceDefinition(name = "ClassA", module = "module-a", extendsList = Seq("ClassB")))
    }
  }
}
