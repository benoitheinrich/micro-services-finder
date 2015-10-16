package net.bhservices.microservices.finder

import net.bhservices.microservices.finder.Application.Config

/**
 * Utility use to log verbose messages to standard output.
 */
object VerboseLogger {
  /**
   * Logs a message if the verbose mode is enabled
   * @param msg the message to log.
   */
  def log(msg: => String): Unit = {
    Application.config match {
      case Some(c: Config) if c.verbose => println(msg)
      case _ =>
    }
  }
}
