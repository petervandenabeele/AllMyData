/**
 * Created by peter_v on 02/12/14.
 */

package encoding

import java.time.LocalDateTime

import base.Fact
import kafkaStreaming.KafkaProducer
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactDecoderSuite extends FunSuite {

  trait testFooBar {
    val subject = "88301684-3859-4f70-8f90-2c7a90256268"
    val timePoint1: LocalDateTime = LocalDateTime.now()
    val fact1 =
      Fact(subject = subject,
        predicate = "atd:foo",
        objectType = "s",
        objectValue = "Bar",
        timeStamp = timePoint1.toString)
    val fact2 =
      Fact(subject = subject,
        predicate = "atd:bar",
        objectType = "s",
        objectValue = "Tux\nPing,\\ , \n , , ,",
        timeStamp = timePoint1.toString)
    val factEncoder = new FactEncoder()
    val encoded_fact1 = factEncoder.toBytes(fact1)
    val encoded_fact2 = factEncoder.toBytes(fact2)
  }

  test("FactDecoder can be instantiated") {
    new FactDecoder()
  }

  test("FactDecoder does correct decoding") {
    new testFooBar {
      val factDecoder = new FactDecoder()
      val decoded_fact1 = factDecoder.fromBytes(encoded_fact1)
      assert(decoded_fact1.timeStamp === fact1.timeStamp)
      assert(decoded_fact1.uuid === fact1.uuid)
      assert(decoded_fact1.subject === fact1.subject)
      assert(decoded_fact1.objectType === fact1.objectType)
      assert(decoded_fact1.objectValue === fact1.objectValue)
    }
  }

  test("FactDecoder does correct decoding with newline and comma and backslash in the string") {
    new testFooBar {
      val factDecoder = new FactDecoder()
      val decoded_fact2 = factDecoder.fromBytes(encoded_fact2)
      assert(decoded_fact2.timeStamp === fact2.timeStamp)
      assert(decoded_fact2.uuid === fact2.uuid)
      assert(decoded_fact2.subject === fact2.subject)
      assert(decoded_fact2.objectType === fact2.objectType)
      assert(decoded_fact2.objectValue === fact2.objectValue)

      val kafkaProducer = KafkaProducer(brokerList = "localhost:9092")
      kafkaProducer.send(encoded_fact2, null)
    }
  }
}
