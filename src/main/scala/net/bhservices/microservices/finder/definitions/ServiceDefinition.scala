package net.bhservices.microservices.finder.definitions

/**
 * Contains details of a service definition.
 *
 * @param name the name of the class identifying the service.
 * @param module the name of the module containing that service.
 * @param extendsList the list of services that are extended by this service.
 */
case class ServiceDefinition(name: String,
                             module: String,
                             extendsList: Seq[String] = Seq.empty)
