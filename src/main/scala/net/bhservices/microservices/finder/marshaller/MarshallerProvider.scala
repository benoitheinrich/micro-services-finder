package net.bhservices.microservices.finder.marshaller

import scala.reflect.Manifest

/**
 *
 */
trait MarshallerProvider[T <: AnyRef] {
  def marshaller(implicit m: Manifest[T]): Marshaller

  trait Marshaller {
    def marshal(t: T): String

    def unmarshal(content: String): T
  }

}
