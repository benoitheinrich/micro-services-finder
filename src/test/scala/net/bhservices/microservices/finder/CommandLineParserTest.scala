package net.bhservices.microservices.finder

import java.io.File

import net.bhservices.microservices.finder.config.ConfigProvider.Config
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class CommandLineParserTest extends FeatureSpec with GivenWhenThen with Matchers {

  info("As a User")
  info("I want to be able to pass arguments to the command line")

  feature("Passing arguments to the command line") {
    scenario("User doesn't pass any arguments") {
      Given("a user doesn't pass any arguments")
      val args = Seq()

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should be using all defaults")
      config shouldBe Some(Config())
    }

    // This scenario has to be ignored as right now scopt does allow to override the sys.exit when help is called
    ignore("User requires help") {
      Given("a user pass the --help argument")
      val args = Seq("--help")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should not be returned as help message was printed")
      config shouldBe empty
    }

    scenario("User wants to clear caches") {
      Given("a user pass the --clear argument")
      val args = Seq("--clear")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should contain the clear flag")
      config shouldBe Some(Config(clear = true))
    }

    scenario("User wants to select the output format") {
      Given("a user pass the --format argument")
      val args = Seq("--format", "png")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should contain the selected format")
      config shouldBe Some(Config(format = "png"))
    }

    scenario("User wants to select the output directory") {
      Given("a user pass the --output argument")
      val args = Seq("--output", "/tmp")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should contain the selected output directory")
      config shouldBe Some(Config(output = new File("/tmp")))
    }

    scenario("User wants to select the source directory") {
      Given("a user pass the --source argument")
      val args = Seq("--source", "/tmp")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should contain the selected source directory")
      config shouldBe Some(Config(source = new File("/tmp")))
    }

    scenario("User wants to enable the verbose mode") {
      Given("a user pass the --verbose argument")
      val args = Seq("--verbose")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should contain the verbose flag")
      config shouldBe Some(Config(verbose = true))
    }

    scenario("User selects an invalid source directory") {
      Given("a user pass an invalid --source argument")
      val args = Seq("--source", "/tmp/abc/def/")

      When("the command line is started")
      val config = new CommandLineParser().parseConfig(args)

      Then("the config should fail")
      config shouldBe empty
    }
  }
}
