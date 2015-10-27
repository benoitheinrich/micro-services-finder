package net.bhservices.microservices.finder.definitions

/**
 * Base definition of a service definition scanner.
 */
trait ServiceDefinitionScannerProvider {
  def scanner: ServiceDefinitionScanner

  trait ServiceDefinitionScanner {
    /**
     * Scan the definitions from the scanner.
     * @return the list of scanned definitions.
     */
    def scanDefinitions: Seq[ServiceDefinition]
  }

}
