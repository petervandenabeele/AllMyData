/**
  * Created by peter_v on 08/23/15.
  */

package csv

import base.Context
import common.{FactWithStatusIterator, FactWithStatus}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import FactsReader.reader

@RunWith(classOf[JUnitRunner])
class FactsReaderSuite extends FunSuite {

    test("Object FactsReader can read an empty CSV file") {
      val filename = "/empty_CSV_file.csv"
      val file = scala.io.Source.fromURL(getClass.getResource(filename))
      val factWithStatusIterator = reader(file)
      assert(factWithStatusIterator.isEmpty)
    }

    test("Object InFactsReader can read a simple CSV file") {
      val filename = "/fact_csv/simple_fact_CSV_file.csv"
      val file = scala.io.Source.fromURL(getClass.getResource(filename))
      val factWithStatusIterator: FactWithStatusIterator = reader(file)

      val factWithStatus_0: FactWithStatus = factWithStatusIterator.next()
      val fact_0 = factWithStatus_0._1.get
      assert(fact_0.timestamp.toString.startsWith("2014")) // not newly made
      assert(fact_0.id.toString.startsWith("3d83fa7e"))
      assert(fact_0.context == Context(None))
      assert(fact_0.subject.toString.startsWith("973a7688"))
      assert(fact_0.predicate === "amd:foo")
      assert(fact_0.objectType === "s")
      assert(fact_0.objectValue === "Foo")

      val factWithStatus_1: FactWithStatus = factWithStatusIterator.next()
      val fact_1 = factWithStatus_1._1.get
      assert(fact_1.timestamp.toString.startsWith("2014"))
      assert(fact_1.id.toString.startsWith("c0994ce6"))
      assert(fact_1.context == Context(fact_0.subject.toString))
      assert(fact_1.subject.toString.startsWith("5458822b"))
      assert(fact_1.predicate === "amd:bar")
      assert(fact_1.objectType === "s")
      assert(fact_1.objectValue === "Bar")
    }

}
