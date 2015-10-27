package net.bhservices.microservices.finder.definitions

import net.bhservices.microservices.finder.VerboseLogger
import net.bhservices.microservices.finder.cache.CacheProvider
import net.bhservices.microservices.finder.config.ConfigProvider

/**
 * Service used to generate service definitions.
 */
trait ServiceDefinitionsRepositoryFactoryProvider {
  deps: CacheProvider[Seq[ServiceDefinition]] with ConfigProvider =>

  val factory = new ServiceDefinitionsRepositoryFactory()

  class ServiceDefinitionsRepositoryFactory {

    /**
     * Generate the list of definitions if needed based on the configuration and the fact that index files might not exist.
     * @return the service definition repository
     */
    def generateIfNeeded(): ServiceDefinitionsRepository = {
      if (config.clear || !deps.cache.isCacheAvailable) {
        VerboseLogger.log(s"scanning service definitions from ${config.source}")
        val definitions = scanDefinitions
        new ServiceDefinitionsRepository(definitions)
      } else {
        val definitions = deps.cache.load
        new ServiceDefinitionsRepository(definitions)
      }
    }

    /**
     * Save the definitions to json
     * @param repository the repository containing the definitions to be saved.
     */
    def saveDefinitions(repository: ServiceDefinitionsRepository): Unit = {
      cache.save(repository.definitions)
    }

    /**
     * Scan the definitions from the source directory.
     * @return the list of scanned definitions.
     */
    private def scanDefinitions: Seq[ServiceDefinition] = {
      Seq(ServiceDefinition(
        name = "test",
        module = "test.jar"
      ))
    }
  }

}