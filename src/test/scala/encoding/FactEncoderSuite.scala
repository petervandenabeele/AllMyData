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
class FactEncoderSuite extends FunSuite {

  trait testFooBar {
    val subject = "88301684-3859-4f70-8f90-2c7a90256268"
    val fact1 =
      Fact(subject = subject,
        predicate = "atd:foo",
        objectType = "s",
        objectValue = "Bar")
    val fact2 =
      Fact(subject = subject,
        predicate = "atd:bar",
        objectType = "s",
        objectValue = "Tux\nPing")
  }

  test("FactEncoder can be instantiated") {
    new FactEncoder()
  }

  test("FactEncoder does encoding") {
    new testFooBar {
      val factEncoder = new FactEncoder()
      val kafkaProducer = KafkaProducer()
      val partition = null

      val encoded = factEncoder.toBytes(fact1)
    }
  }

  test("FactEncoder does encoding with newline in the string") {
    new testFooBar {
      val factEncoder = new FactEncoder()
      val kafkaProducer = KafkaProducer()

      val endoced = factEncoder.toBytes(fact2)
    }
  }

  test("FactEncoder result can be sent on KafkaProducer") {
    new testFooBar {
      val factEncoder = new FactEncoder()
      val kafkaProducer = KafkaProducer()
      kafkaProducer.send(factEncoder.toBytes(fact2), null)
    }
  }
}
