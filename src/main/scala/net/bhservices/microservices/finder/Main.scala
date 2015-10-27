package net.bhservices.microservices.finder

import java.io.File

import net.bhservices.microservices.finder.cache.FileCacheProvider
import net.bhservices.microservices.finder.config.ConfigProvider
import net.bhservices.microservices.finder.definitions.{ServiceDefinition, ServiceDefinitionsRepositoryFactoryProvider}
import net.bhservices.microservices.finder.marshaller.JsonMarshallerProvider

object Main extends App {

  new CommandLineParser().parseConfig(args) match {
    case Some(config) =>
      Application.setConfig(config)

      object Dependencies
        extends ServiceDefinitionsRepositoryFactoryProvider
        with FileCacheProvider[Seq[ServiceDefinition]]
        with JsonMarshallerProvider[Seq[ServiceDefinition]]
        with ConfigProvider {

        override def config = Application.config.get

        override val cacheFile = new File(config.source, "service-definitions.json")
      }

      val factory = Dependencies.factory
      val serviceDefinitionsRepository = factory.generateIfNeeded()
      factory.saveDefinitions(serviceDefinitionsRepository)

    case None =>
  }
}