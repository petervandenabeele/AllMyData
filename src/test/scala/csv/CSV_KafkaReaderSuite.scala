/**
  * Created by peter_v on 08/23/15.
  */

package csv

import java.util.UUID

import base.{Context, Fact}
import common.{FactIterator, FactWithStatus}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CSV_KafkaReaderSuite extends FunSuite {

  //  test("Object CSV_KafkaReader can read an empty CSV file") {
  //    val filename = "/empty_CSV_file.csv"
  //    val file = scala.io.Source.fromURL(getClass.getResource(filename))
  //    val factIterator = reader(file)
  //    assert(factIterator.isEmpty)
  //  }
  //
  //  test("Object CSV_InFactReader can read a simple CSV file") {
  //    val filename = "/kafka_csv/simple_kafka_CSV_file.csv"
  //    val file = scala.io.Source.fromURL(getClass.getResource(filename))
  //    val factIterator: FactIterator = reader(file)
  //
  //    val factWithStatus: FactWithStatus = factIterator.next()
  //    val fact_1 = factWithStatus._1.get
  //    assert(fact_1.timeStamp.toString.startsWith("2014")) // not newly made
  //    assert(fact_1.uuid.toString.startsWith("3d83fa7e"))
  //    assert(fact_1.context == Context(None))
  //    assert(fact_1.subject.toString.startsWith("973a7688"))
  //    assert(fact_1.predicate === "amd:foo")
  //    assert(fact_1.objectType === "s")
  //    assert(fact_1.objectValue === "Foo")
  //
  //    val fact_2 = factIterator.next()._1.get
  //    assert(fact_2.timeStamp.toString.startsWith("2014"))
  //    assert(fact_2.uuid.toString.startsWith("c0994ce6"))
  //    assert(fact_2.context == Context(fact_1.subject.toString))
  //    assert(fact_2.subject.toString.startsWith("5458822b"))
  //    assert(fact_2.predicate === "amd:bar")
  //    assert(fact_2.objectType === "s")
  //    assert(fact_2.objectValue === "Bar")
  //  }

}
