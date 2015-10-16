package net.bhservices.microservices.finder

import java.io.File

object Application {
  private def currentDir: File = {
    new File(System.getProperty("user.dir"))
  }

  /**
   * Configuration of the parsed item
   */
  case class Config(clear: Boolean = false,
                    format: String = "dot",
                    output: File = currentDir,
                    source: File = currentDir,
                    verbose: Boolean = false)

  private var currentConfig: Option[Config] = _

  def setConfig(config: Config): Unit = {
    currentConfig = Some(config)
    VerboseLogger.log(s"Current configuration set to $config")
  }

  /**
   * Get the current configuration set from the command line
   */
  def config: Option[Config] = {
    currentConfig
  }
}
