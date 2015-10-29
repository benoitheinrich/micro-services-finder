package net.bhservices.microservices.finder

import java.io.File

import net.bhservices.microservices.finder.cache.FileCacheProvider
import net.bhservices.microservices.finder.config.ConfigProvider
import net.bhservices.microservices.finder.definitions.{ServiceDefinition, ServiceDefinitionsRepositoryFactoryProvider, SourcePathServiceDefinitionScannerProvider}
import net.bhservices.microservices.finder.marshaller.JsonMarshallerProvider

object Main extends App {

  new CommandLineParser().parseConfig(args) match {
    case Some(config) =>
      Application.setConfig(config)

      object Dependencies
        extends ServiceDefinitionsRepositoryFactoryProvider
        with FileCacheProvider[Seq[ServiceDefinition]]
        with JsonMarshallerProvider[Seq[ServiceDefinition]]
        with SourcePathServiceDefinitionScannerProvider
        with ConfigProvider {

        override def config = Application.config.get

        override val cacheFile = new File(config.source, "service-definitions.json")

        override def sourcePath = config.source
      }

      val factory = Dependencies.factory
      val serviceDefinitionsRepository = factory.generateIfNeeded()
      factory.saveDefinitions(serviceDefinitionsRepository)

    case None =>
  }
}