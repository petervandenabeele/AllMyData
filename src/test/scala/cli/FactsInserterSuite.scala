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

  test("one_fact.csv creates 3 facts for one resource") {
    val filename = "/one_fact.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator = reader(file)
    val facts: Array[Fact] = factIterator.toArray
    val fact_1 = facts(0)
    val fact_2 = facts(1)
    val fact_3 = facts(2)

    assert(facts.size === 3)

    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.predicate === "atd:tux")
    assert(fact_2.objectType === "s")
    assert(fact_2.objectValue === "ping")

    assert(fact_3.predicate === "atd:ping")
    assert(fact_3.objectType === "s")
    assert(fact_3.objectValue === "pong")

    val resource_uuid = fact_1.uuid
    assert(fact_2.uuid === resource_uuid)
    assert(fact_3.uuid === resource_uuid)
  }
}
