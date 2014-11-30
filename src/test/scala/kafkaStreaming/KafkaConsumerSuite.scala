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

  trait aProducer {
    val kafkaProducer: KafkaProducer = KafkaProducer(brokerList = "localhost:9092")
  }

  trait aConsumer {
    val kafkaConsumer: KafkaConsumer = KafkaConsumer()
  }


  test("Can read a message from ATD_test") {
    def puts(msg: String): Unit = {
      println("Read as Kafka consumer on ATD_test : " + msg)
    }

    new aProducer {
      kafkaProducer.send("foobar123")
    }

    new aConsumer {
      val result = kafkaConsumer.read(x => puts(x))
      assert(result === "break on foobar123")
    }
  }
}
