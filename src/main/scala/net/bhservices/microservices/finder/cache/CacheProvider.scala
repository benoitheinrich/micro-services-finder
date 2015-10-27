package net.bhservices.microservices.finder.cache

import scala.reflect.Manifest

/**
 * A provider for a cache of a given object type `T`.
 */
trait CacheProvider[T <: AnyRef] {
  def cache(implicit m: Manifest[T]): Cache

  trait Cache {
    def isCacheAvailable: Boolean

    def load: T

    def save(t: T): Unit
  }

}
