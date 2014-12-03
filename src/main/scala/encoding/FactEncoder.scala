/**
 * Created by peter_v on 02/12/14.
 */

package encoding

import base.Fact
import kafka.serializer.Encoder
import kafka.utils.VerifiableProperties

class FactEncoder(props: VerifiableProperties = null) extends Encoder[Fact]{
  val encoding =
    if(props == null)
      "UTF8"
    else
      props.getString("serializer.encoding", "UTF8")

  override def toBytes(fact: Fact): Array[Byte] =
    if(fact == null)
      null
    else {
      // Under the presumption that only the object
      // may contain comma's and newlines, this is
      // a sub optimal, but _working_ enconding ...
      val raw = fact.toString
      raw.substring(5, raw.length()-1).getBytes(encoding)
    }
}
