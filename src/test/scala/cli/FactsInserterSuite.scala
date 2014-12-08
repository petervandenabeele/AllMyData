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

  test("two_facts.csv creates 2 resources with 3 facts each") {
    val filename = "/two_facts.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator = reader(file)
    val facts: Array[Fact] = factIterator.toArray

    // scrambled order (but it still works :-)
    val fact_3 = facts(4)
    val fact_1 = facts(0)
    val fact_2 = facts(2)
    val fact_4 = facts(1)
    val fact_5 = facts(3)
    val fact_6 = facts(5)

    assert(facts.size === 6)

    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.predicate === "atd:tux")
    assert(fact_2.objectType === "s")
    assert(fact_2.objectValue === "ping")

    assert(fact_3.predicate === "atd:ping")
    assert(fact_3.objectType === "s")
    assert(fact_3.objectValue === "pong")

    assert(fact_4.predicate === "atd:bar")
    assert(fact_4.objectType === "s")
    assert(fact_4.objectValue === "foo")

    assert(fact_5.predicate === "atd:ping")
    assert(fact_5.objectType === "s")
    assert(fact_5.objectValue === "tux")

    assert(fact_6.predicate === "atd:pong")
    assert(fact_6.objectType === "s")
    assert(fact_6.objectValue === "ping")

    val resource_uuid_1 = fact_1.uuid
    assert(fact_2.uuid === resource_uuid_1)
    assert(fact_3.uuid === resource_uuid_1)

    val resource_uuid_2 = fact_4.uuid
    assert(fact_5.uuid === resource_uuid_2)
    assert(fact_6.uuid === resource_uuid_2)
  }
}
