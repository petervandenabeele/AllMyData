/**
 * Created by peter_v on 23/11/14.
 */

package kafkaStreaming

import java.time.LocalDateTime

import base.Fact
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class KafkaConsumerSuite extends FunSuite {

  trait testPeter {
    val subject = "88301684-3859-4f70-8f90-2c7a90256268"
    val timePoint1: LocalDateTime = LocalDateTime.now()
    val fact1 =
      Fact(subject = subject,
           predicate = "atd:first_name",
           objectType = "s",
           objectValue = "Peter",
           timeStamp = timePoint1.toString)

    val timePoint2: LocalDateTime = LocalDateTime.now()
    val fact2 =
      Fact(subject = subject,
           predicate = "atd:last_name",
           objectType = "s",
           objectValue = "V",
           timeStamp = timePoint2.toString)
  }

  trait aProducer {
    val kafkaProducer: KafkaProducer = KafkaProducer(brokerList = "localhost:9092")
  }

  trait aConsumer {
    val kafkaConsumer: KafkaConsumer = KafkaConsumer()
  }

  test("Can create a KafkaConsumer") {
    new aConsumer {
    }
  }

  test("Can read a message from ATD_test") {
    def puts(msg: String): Unit = {
      println("Read as Kafka consumer on ATD_test : " + msg)
    }

    new aConsumer {
      new aProducer {
        new testPeter {
          kafkaProducer.send(fact1.toString)
          kafkaProducer.send(fact2.toString)
          kafkaProducer.send("foobar123")
          val result = kafkaConsumer.read(x => puts(x))
          assert(result === "break on foobar123")
        }
      }
    }
  }
}
