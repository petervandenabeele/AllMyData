/**
 * Created by peter_v on 23/11/14.
 */

package kafkaStreaming

import base.Fact
import encoding.FactEncoder
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class KafkaProducerSuite extends FunSuite {

  trait testFooBar {
    val fact1 =
      Fact(predicate = "atd:foo",
           objectType = "s",
           objectValue = "Bar")
    val fact2 =
      Fact(predicate = "atd:tux",
           objectType = "s",
           objectValue = "Ping")
  }

  trait aProducer {
    val kafkaProducer: KafkaProducer = KafkaProducer()
  }

  test("Can create a KafkaProducer") {
    new aProducer {
    }
  }

  test("Can send a message to ATD_test") {
    new aProducer {
      new testFooBar {
        val factEncoder = new FactEncoder()

        kafkaProducer.send(factEncoder.toBytes(fact1), null)
        kafkaProducer.send(factEncoder.toBytes(fact2), null)
      }
    }
  }
}
