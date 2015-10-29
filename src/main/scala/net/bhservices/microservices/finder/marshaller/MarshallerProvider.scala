package net.bhservices.microservices.finder.marshaller

import scala.reflect.Manifest

/**
 * A provider for Marshalling some data to string and from string.
 */
trait MarshallerProvider[T] {
  def marshaller(implicit m: Manifest[T]): Marshaller

  trait Marshaller {
    def marshal(t: T): String

    def unmarshal(content: String): T
  }

}
