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

  trait testFoo {
    val subject = "88301684-3859-4f70-8f90-2c7a90256268"
    val timePoint1: LocalDateTime = LocalDateTime.now()
    val fact1 =
      Fact(subject = subject,
        predicate = "atd:foo",
        objectType = "s",
        objectValue = "Bar",
        timeStamp = timePoint1.toString)
  }

  test("new FactEncoder() works") {
    new FactEncoder()
  }

  test("new FactEncoder() can do encoding") {
    new testFoo {
      val factEncoder = new FactEncoder()
      val kafkaProducer = KafkaProducer(brokerList = "localhost:9092")
      val partition = null

      kafkaProducer.send(factEncoder.toBytes(fact1), partition)
    }
  }
}
