package net.bhservices.microservices.finder.config

import java.io.File

import net.bhservices.microservices.finder.FileUtility.currentDir


object ConfigProvider {
  /**
   * Configuration of the parsed item
   */
  case class Config(clear: Boolean = false,
                    format: String = "dot",
                    output: File = currentDir,
                    source: File = currentDir,
                    verbose: Boolean = false)

}

/**
 * A provider for a config object.
 */
trait ConfigProvider {

  import ConfigProvider.Config

  def config: Config
}
