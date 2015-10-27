package net.bhservices.microservices.finder

import net.bhservices.microservices.finder.config.ConfigProvider


object Application {

  import ConfigProvider._

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
