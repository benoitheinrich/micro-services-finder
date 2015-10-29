package net.bhservices.microservices.finder.cache

import java.io.{File, PrintWriter}

import net.bhservices.microservices.finder.VerboseLogger
import net.bhservices.microservices.finder.marshaller.MarshallerProvider

import scala.io.Source
import scala.reflect.Manifest

/**
 * Provider which store the cache in the given `cacheFile`.
 */
trait FileCacheProvider[T <: AnyRef] extends CacheProvider[T] {
  deps: MarshallerProvider[T] =>

  def cacheFile: File

  override final def cache(implicit m: Manifest[T]): Cache = new FileCache()

  private final class FileCache(implicit m: Manifest[T]) extends Cache {
    private var cachedContent: Option[T] = None

    override def isCacheAvailable = cacheFile.canRead && cacheFile.length() > 0

    override def load: T = {
      this.synchronized {
        cachedContent match {
          case Some(t) => t
          case None =>
            // Read from the cache
            VerboseLogger.log(s"reading service definitions cache from $cacheFile")
            val content = Source.fromFile(cacheFile).mkString
            val t = marshaller.unmarshal(content)
            cachedContent = Some(t)
            t
        }
      }
    }

    override def save(t: T) = {
      VerboseLogger.log(s"saving service definitions cache to $cacheFile")
      val json = marshaller.marshal(t)
      new PrintWriter(cacheFile) {
        write(json)
        close()
      }
    }
  }

}
