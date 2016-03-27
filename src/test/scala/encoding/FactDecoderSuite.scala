/**
 * Created by peter_v on 02/12/14.
 */

package encoding

import java.time.LocalDateTime
import java.util.UUID

import base.{Context, Fact}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactDecoderSuite extends FunSuite {

//  trait testFooBar {
//    val subject = UUID.fromString("88301684-3859-4f70-8f90-2c7a90256268")
//    val context = Context("99412745-496a-4081-8f90-2c7a90256269")
//    val fact_1 = Fact(
//      subject = subject,
//      predicate = "atd:foo",
//      objectType = "s",
//      objectValue = "Bar")
//    val fact_2 = Fact(
//      context = context,
//      subject = subject,
//      predicate = "atd:bar",
//      objectType = "s",
//      objectValue = "Tux\nPing,\\ , \n , , ,")
//    val factEncoder = new FactEncoder()
//    val encoded_fact_1 = factEncoder.toBytes(fact_1)
//    val encoded_fact_2 = factEncoder.toBytes(fact_2)
//  }
//
//  test("FactDecoder can be instantiated") {
//    new FactDecoder()
//  }
//
//  test("FactDecoder does correct decoding") {
//    new testFooBar {
//      val factDecoder = new FactDecoder()
//      val decoded_fact_1 = factDecoder.fromBytes(encoded_fact_1)
//      assert(decoded_fact_1.timeStamp.startsWith("20"))
//      assert(decoded_fact_1.uuid === fact_1.uuid)
//      assert(decoded_fact_1.context === fact_1.context)
//      assert(decoded_fact_1.subject === fact_1.subject)
//      assert(decoded_fact_1.objectType === fact_1.objectType)
//      assert(decoded_fact_1.objectValue === fact_1.objectValue)
//    }
//  }
//
//  test("FactDecoder does correct decoding with newline and comma and backslash in the string") {
//    new testFooBar {
//      val factDecoder = new FactDecoder()
//      val decoded_fact_2 = factDecoder.fromBytes(encoded_fact_2)
//      assert(decoded_fact_2.timeStamp.startsWith("20"))
//      assert(decoded_fact_2.uuid === fact_2.uuid)
//      assert(decoded_fact_2.context === fact_2.context)
//      assert(decoded_fact_2.subject === fact_2.subject)
//      assert(decoded_fact_2.objectType === fact_2.objectType)
//      assert(decoded_fact_2.objectValue === fact_2.objectValue)
//
//      val kafkaProducer = KafkaProducer()
//      kafkaProducer.send(encoded_fact_2, null)
//    }
//  }
}
