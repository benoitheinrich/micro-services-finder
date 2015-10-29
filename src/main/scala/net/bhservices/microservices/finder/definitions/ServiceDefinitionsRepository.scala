package net.bhservices.microservices.finder.definitions

object ServiceDefinitionsRepository {
  def index(defs: Seq[ServiceDefinition]): Map[ServiceDefinitionName, ServiceDefinition] = {
    defs.iterator.map { e =>
      (e.name, e)
    } toMap
  }
}

/**
 * Repository containing all service definitions.
 */
class ServiceDefinitionsRepository(val definitions: Map[ServiceDefinitionName, ServiceDefinition]) {
}
