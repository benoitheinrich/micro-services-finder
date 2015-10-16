package net.bhservices.microservices.finder

import java.io.File

/**
 * Parser for the CommandLine argument
 */
class CommandLineParser {

  import net.bhservices.microservices.finder.Application.Config
  import scopt.OptionParser


  private def format(str: String): String = {
    def wrap(input: String, maxLength: Int) = {
      input.split(" ").foldLeft(("", 0))(
        (acc, in) =>
          if (in equals "") acc
          else if ((acc._2 + in.length()) < maxLength) {
            (acc._1 + " " + in, acc._2 + in.length())
          }
          else {
            (acc._1 + '\n' + in, in.length())
          })._1.trim
    }

    val INDENT = " " * 8
    val singleLine = str.stripMargin.replaceAll("\n", " ")
    val wrappedText = wrap(singleLine, 70)
    val wrappedLines = wrappedText split "\n"
    val indentedLines = wrappedLines mkString "\n" + INDENT

    indentedLines
  }

  private val parser = new OptionParser[Config]("micro-services-finder") {

    opt[Unit]('c', "clear") action { (value, c) =>
      c.copy(clear = true)
    } text format(
      """
        |If specified indicates the tool should clear
        |all caches prior running.
      """)
    note("")

    opt[String]('f', "format") action { (value, c) =>
      c.copy(format = value)
    } text format(
      """
        |If specified indicates the output format to
        |be used by the tool.  Format are passed
        |directly to the graphviz command to use and
        |accepts any value as supported by your
        |graphviz installation.
        |By default it generates a .dot file format.
      """)
    note("")

    opt[File]('o', "output") action { (value, c) =>
      c.copy(output = value)
    } text format(
      """
        |If specified indicates the place where the
        |generated files will be stored.
        |By default it generates them in the current
        |directory.
        |The -o option is also used to indicate where
        |the index files will be generated.
      """)
    note("")

    opt[File]('s', "source") action { (value, c) =>
      c.copy(source = value)
    } text format(
      """
        |If specified indicates where to analyse
        |source code from.  By default it analyses
        |the source code from the current directory.
        |The -s option is also used to indicate where
        |to locate the configuration file to be used
        |by the tool.
      """)
    note("")

    opt[Unit]('v', "verbose") action { (value, c) =>
      c.copy(verbose = true)
    } text format(
      """
        |If specified indicates the tool needs to
        |generate useful information to the reader
        |to understand what's happening.
      """)
    note("")

    help("help") text "prints this usage text"

    checkConfig { c =>
      if (!c.source.exists()) failure(s"source should be readable: ${c.source}")
      else if (!c.source.canRead) failure(s"source should be readable: ${c.source}")
      else if (!c.source.isDirectory) failure(s"source should point to a directory: ${c.source}")
      else if (!c.output.canRead) failure(s"output should be readable: ${c.output}")
      else if (!c.output.canWrite) failure(s"output should be writable: ${c.output}")
      else if (!c.output.isDirectory) failure(s"output should point to a directory: ${c.output}")
      else success
    }
  }

  def parseConfig(args: Seq[String]): Option[Config] = {
    parser.parse(args, Config())
  }
}
