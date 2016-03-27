/**
 * Created by peter_v on 23/11/14.
 */

package kafkaStreaming

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class KafkaConsumerSuite extends FunSuite {

//  trait aProducer {
//    val sentinel = "foobar125"
//    val kafkaProducer: KafkaProducer = KafkaProducer()
//    kafkaProducer.send(sentinel)
//  }
//
//  trait aConsumer {
//    val kafkaConsumer: KafkaConsumer = KafkaConsumer()
//  }
//
//  // ignore for now, since depends on stable ip address
//  test("Can read a message from ATD_test") {
//    def puts(msg: String): Unit = {
//      println("Read as Kafka consumer on ATD_test : " + msg)
//    }
//
//    new aConsumer {
//      new aProducer {
//        val result = kafkaConsumer.read(x => puts(x))
//        assert(result === sentinel)
//      }
//    }
//  }
}
