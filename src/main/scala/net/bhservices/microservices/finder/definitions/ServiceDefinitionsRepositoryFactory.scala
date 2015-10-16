package net.bhservices.microservices.finder.definitions

import java.io.{File, PrintWriter}

import net.bhservices.microservices.finder.Application.Config
import net.bhservices.microservices.finder.VerboseLogger

import scala.io.Source

/**
 * Service use to generate service definitions.
 */
class ServiceDefinitionsRepositoryFactory(config: Config) {

  private val cacheFile = new File(config.source, "service-definitions.json")

  /**
   * Generate the list of definitions if needed based on the configuration and the fact that index files might not exist.
   * @return the service definition repository
   */
  def generateIfNeeded(): ServiceDefinitionsRepository = {
    if (config.clear || cacheNotAvailable()) {
      VerboseLogger.log(s"scanning service definitions from ${config.source}")
      val definitions = scanDefinitions
      new ServiceDefinitionsRepository(definitions)
    } else {
      // Read from the cache
      import org.json4s._
      import org.json4s.native.Serialization.read
      implicit val formats = DefaultFormats
      VerboseLogger.log(s"reading service definitions cache from $cacheFile")
      val content = Source.fromFile(cacheFile).mkString
      val definitions = read[Seq[ServiceDefinition]](content)
      new ServiceDefinitionsRepository(definitions)
    }
  }

  /**
   * Save the definitions to json
   * @param repository the repository containing the definitions to be saved.
   */
  def saveDefinitions(repository: ServiceDefinitionsRepository): Unit = {
    import org.json4s._
    import org.json4s.native.Serialization.write
    implicit val formats = DefaultFormats
    VerboseLogger.log(s"saving service definitions cache to $cacheFile")

    val json = write(repository.definitions)

    new PrintWriter(cacheFile) {
      write(json)
      close()
    }
  }

  /**
   * Check if cache is not available.
   * @return true if cache is not available.
   */
  private def cacheNotAvailable(): Boolean = {
    !cacheFile.canRead
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
