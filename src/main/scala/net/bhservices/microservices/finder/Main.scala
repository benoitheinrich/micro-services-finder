package net.bhservices.microservices.finder

object Main extends App {

  new CommandLineParser().parseConfig(args) match {
    case Some(config) =>
      Application.setConfig(config)

    case None =>
  }
}