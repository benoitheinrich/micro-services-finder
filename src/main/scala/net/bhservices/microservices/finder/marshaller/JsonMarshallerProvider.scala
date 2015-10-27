package net.bhservices.microservices.finder.marshaller

import scala.reflect.Manifest

/**
 * A marshaller that uses JSON format.
 */
trait JsonMarshallerProvider[T <: AnyRef] extends MarshallerProvider[T] {
  override def marshaller(implicit m: Manifest[T]): Marshaller = new JsonMarshaller

  private final class JsonMarshaller(implicit m: Manifest[T]) extends Marshaller {
    override def marshal(t: T): String = {
      import org.json4s._
      import org.json4s.native.Serialization.write
      implicit val formats = DefaultFormats
      write[T](t)
    }

    override def unmarshal(content: String): T = {
      import org.json4s._
      import org.json4s.native.Serialization.read
      implicit val formats = DefaultFormats
      read[T](content)
    }
  }

}
