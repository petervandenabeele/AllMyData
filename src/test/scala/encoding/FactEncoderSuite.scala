/**
 * Created by peter_v on 02/12/14.
 */

package encoding

import java.util.UUID

import base.{Context, Fact}

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactEncoderSuite extends FunSuite {

//  trait testFooBar {
//    val subject = UUID.fromString("88301684-3859-4f70-8f90-2c7a90256268")
//    val context = Context("99412745-496a-4081-8f90-2c7a90256269")
//    val fact_1 = Fact(
//          subject = subject,
//          predicate = "atd:foo",
//          objectType = "s",
//          objectValue = "Bar")
//    val fact_2 = Fact(
//          context = context,
//          subject = subject,
//          predicate = "atd:bar",
//          objectType = "s",
//          objectValue = "Tux\nPing , , ")
//  }
//
//  test("FactEncoder can be instantiated") {
//    new FactEncoder()
//  }
//
//  test("FactEncoder does encoding") {
//    new testFooBar {
//      val factEncoder = new FactEncoder()
//      val encoded = factEncoder.toBytes(fact_1)
//    }
//  }
//
//  test("FactEncoder does encoding with newline, comma, context") {
//    new testFooBar {
//      val factEncoder = new FactEncoder()
//      val encoded = factEncoder.toBytes(fact_2)
//    }
//  }
//
//  test("FactEncoder result can be sent on KafkaProducer") {
//    new testFooBar {
//      val factEncoder = new FactEncoder()
//      val kafkaProducer = KafkaProducer()
//      kafkaProducer.send(factEncoder.toBytes(fact_2), null)
//    }
//  }
}
