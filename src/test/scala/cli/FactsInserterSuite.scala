/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import cli.FactsInserter.reader

@RunWith(classOf[JUnitRunner])
class FactsInserterSuite extends FunSuite {

  test("Object FactsInserter exists") {
    val insterter = FactsInserter
  }

  test("Object FactsInserter can read an empty CSV file") {
    val filename = "/empty_CSV_file.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator = reader(file)
    assert(factIterator.isEmpty)
  }

  test("Object FactsInserter can read a simple CSV file") {
    val filename = "/simple_CSV_file.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator = reader(file)
    val fact: Fact = factIterator.find(fact => true).get

    assert(fact.predicate === "atd:foo")
    assert(fact.objectType === "s")
    assert(fact.objectValue === "bar")
  }
}
