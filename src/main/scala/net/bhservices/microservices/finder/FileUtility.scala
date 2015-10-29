package net.bhservices.microservices.finder

import java.io.File

/**
 * Utilities for files.
 */
object FileUtility {
  /**
   * @return the working directory of the application.
   */
  def currentDir: File = {
    new File(System.getProperty("user.dir"))
  }

  /**
   * Get a recursive stream of files from the given file `f`.
   * @param f the root of the recursive file scanning.
   * @return a recursive stream of files from the given file `f`.
   */
  def getFileTree(f: File): Stream[File] =
    f #:: (if (f.isDirectory) f.listFiles().toStream.flatMap(getFileTree)
    else Stream.empty)
}
