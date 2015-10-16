package net.bhservices.microservices.finder

import net.bhservices.microservices.finder.definitions.ServiceDefinitionsRepositoryFactory

object Main extends App {

  new CommandLineParser().parseConfig(args) match {
    case Some(config) =>
      Application.setConfig(config)
      val factory = new ServiceDefinitionsRepositoryFactory(config)
      val serviceDefinitionsRepository = factory.generateIfNeeded()
      factory.saveDefinitions(serviceDefinitionsRepository)

    case None =>
  }
}