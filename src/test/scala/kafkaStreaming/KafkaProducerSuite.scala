/**
 * Created by peter_v on 23/11/14.
 */

package kafkaStreaming

import java.util.UUID
import java.time.LocalDateTime

import base.Fact
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class KafkaProducerSuite extends FunSuite {

  trait testFooBar {
    val subject = "88301684-3859-4f70-8f90-2c7a90256268"
    val timePoint1: LocalDateTime = LocalDateTime.now()
    val fact1 =
      Fact(subject = subject,
           predicate = "atd:foo",
           objectType = "s",
           objectValue = "Bar",
           timeStamp = timePoint1.toString)

    val timePoint2: LocalDateTime = LocalDateTime.now()
    val fact2 =
      Fact(subject = subject,
           predicate = "atd:tux",
           objectType = "s",
           objectValue = "Ping",
           timeStamp = timePoint2.toString)
  }

  trait aProducer {
    val kafkaProducer: KafkaProducer = KafkaProducer(brokerList = "localhost:9092")
  }

  test("Can create a KafkaProducer") {
    new aProducer {
    }
  }

  test("Can send a message to ATD_test") {
    new aProducer {
      new testFooBar {
        kafkaProducer.send(fact1.toString)
        kafkaProducer.send(fact2.toString)
      }
    }
  }
}
