/**
 * Created by peter_v on 02/12/14.
 */

package encoding

import java.util.UUID

import base.{Context, Fact}
import common.ATD_Context
import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties

class FactDecoder(props: VerifiableProperties = null) extends Decoder[Fact]{
  val encoding =
    if(props == null)
      "UTF8"
    else
      props.getString("serializer.encoding", "UTF8")

  def fromBytes(bytes: Array[Byte]): Fact = {
    val string = new String(bytes, encoding)
    val elements = string.split(",", 7)
    // TODO Context#fromString
    val context: ATD_Context = elements(2) match {
      case "" => Context(None)
      case s: String => Context(Some(UUID.fromString(s)))
    }

    Fact(timeStamp = elements(0),
      uuid = elements(1),
      context = context,
      subject = UUID.fromString(elements(3)),
      predicate = elements(4),
      objectType = elements(5),
      objectValue = elements(6))
  }
}
