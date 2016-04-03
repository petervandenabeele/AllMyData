/**
  * Created by peter_v on 04/12/14.
  */

package csv

import java.util.UUID

import InFactsReader.reader
import base.{Context, Fact}
import common.{FactWithStatusIterator, FactWithStatus}

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InFactsReaderSuite extends FunSuite {

  test("Object InFactsReader can read an empty CSV file") {
    val filename = "/empty_CSV_file.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator = reader(file)
    assert(factWithStatusIterator.isEmpty)
  }

  test("Object InFactsReader can read a simple CSV file") {
    val filename = "/in_fact_csv/simple_CSV_file.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val factWithStatus: FactWithStatus = factWithStatusIterator.next()
    val fact = factWithStatus._1.get

    assert(fact.predicate === "amd:foo")
    assert(fact.objectType === "s")
    assert(fact.objectValue === "bar and café")
    assertResult("2014-11-21T23:59:36.123456789Z") { fact.at.get }
    assertResult("2013-01-01T00:00:00Z") { fact.from.get }
    assertResult("2015-12-31T23:59:59.999Z") { fact.to.get }
  }

  test("one_in_fact.csv creates 3 facts for one resource") {
    val filename = "/in_fact_csv/one_in_fact.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map(p => p._1.get).toArray
    val fact_1 = facts(0)
    val fact_2 = facts(1)
    val fact_3 = facts(2)

    assert(facts.length === 3)

    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.predicate === "amd:dec")
    assert(fact_2.objectType === "d")
    assert(fact_2.objectValue === "6.73")

    assert(fact_3.predicate === "amd:int")
    assert(fact_3.objectType === "i")
    assert(fact_3.objectValue === "42")

    val resource_subject = fact_1.subject
    assert(fact_2.subject === resource_subject)
    assert(fact_3.subject === resource_subject)
  }

  test("one_in_fact_with_context.csv creates 1 context and 3 facts") {
    val filename = "/in_fact_csv/one_in_fact_with_context.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map(p => p._1.get).toArray
    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)
    val fact_3 = facts(4)

    assert(facts.length === 5)

    assert(context_1.predicate === "amd:context:source")
    assert(context_1.objectType === "s")
    assert(context_1.objectValue === "me")

    assert(context_2.predicate === "amd:context:ingress_time")
    assert(context_2.objectType === "s")
    assert(context_2.objectValue === "2014-12-12T22:13:00")

    val expected_context_1 = Context(Some(context_1.subject))
    val expected_context_2 = Context(Some(context_2.subject))
    assert(expected_context_2 === expected_context_1)

    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === expected_context_1)
    assert(fact_2.predicate === "amd:dec")
    assert(fact_2.objectType === "d")
    assert(fact_2.objectValue === "6.5")

    assert(fact_3.context === expected_context_1)
    assert(fact_3.predicate === "amd:int")
    assert(fact_3.objectType === "i")
    assert(fact_3.objectValue === "42")

    val resource_subject = fact_1.subject
    assert(fact_2.subject === resource_subject)
    assert(fact_3.subject === resource_subject)
  }

  test("two_in_facts_with_context.csv 2 contexts for 2 facts") {
    val filename = "/in_fact_csv/two_in_facts_with_context.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map(p => p._1.get).toArray

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)

    assert(facts.length === 4)

    val expected_context_1 = Context(Some(context_1.subject))
    val expected_context_2 = Context(Some(context_2.subject))

    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === expected_context_2)
    assert(fact_2.predicate === "amd:bar")
    assert(fact_2.objectType === "s")
    assert(fact_2.objectValue === "foo")
  }

  test("two_in_facts_with_csv_reference.csv links object first resource") {
    val filename = "/in_fact_csv/two_in_facts_with_csv_reference.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map(p => p._1.get).toArray

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)

    assert(facts.length === 4)

    val expected_context_1 = Context(Some(context_1.subject))
    val expected_context_2 = Context(Some(context_2.subject))


    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === expected_context_2)
    assert(fact_2.predicate === "amd:ref")
    assert(fact_2.objectType === "r")
    assert(UUID.fromString(fact_2.objectValue) === fact_1.subject)
  }

  test("three_in_facts_with_invalid_csv_reference.csv reports error and keeps reading after the invalid fact") {
    val filename = "/in_fact_csv/three_in_facts_with_invalid_csv_reference.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))

    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val factsWithStatusses = factWithStatusIterator.toArray
    val facts: Array[Fact] = factsWithStatusses.filter(p => p._1.nonEmpty).map(p => p._1.get)

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_3 = facts(3)

    val expected_context_1 = Context(Some(context_1.subject))
    val expected_context_2 = Context(Some(context_2.subject))

    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_3.context === expected_context_2)
    assert(fact_3.predicate === "amd:bar")
    assert(fact_3.objectType === "r")
    assert(UUID.fromString(fact_3.objectValue) === fact_1.subject)

    val errors: Array[String] = factsWithStatusses.filter(p => p._1.isEmpty).map(p => p._2.get)

    assert(errors.length === 1)

    val error_1 = errors(0)

    assert(error_1 === "csvObjectValue 15 could not be found")
  }

  test("two_in_facts_with_empty_line_and_csv_reference.csv ignores blank lines") {
    val filename = "/in_fact_csv/two_in_facts_with_empty_line_and_csv_reference.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map{ case (factOption, _) => factOption.get }.toArray

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)

    assert(facts.length === 4)

    val expected_context_1 = Context(Some(context_1.subject))
    val expected_context_2 = Context(Some(context_2.subject))

    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === expected_context_2)
    assert(fact_2.predicate === "amd:bar")
    assert(fact_2.objectType === "r")
    assert(UUID.fromString(fact_2.objectValue) === fact_1.subject)
  }

  test("two_in_facts_with_empty_objectValue for string objectType must be allowed") {
    val filename = "/in_fact_csv/two_in_facts_with_empty_objectValue.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val facts: Array[Fact] = factWithStatusIterator.map{ case (factOption, _) => factOption.get }.toArray

    val context_1 = facts(0)
    val fact_1 = facts(1)

    assert(facts.length === 2)

    val expected_context_1 = Context(Some(context_1.subject))

    assert(fact_1.context === expected_context_1)
    assert(fact_1.predicate === "amd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "")
  }

  test("Object InFactsReader can read a simple CSV file with a ; delimiter and , in data") {
    val filename = "/in_fact_csv/simple_CSV_file_with_semicolon_delimiter.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factWithStatusIterator: FactWithStatusIterator = reader(file)
    val factWithStatus: FactWithStatus = factWithStatusIterator.next()
    val fact = factWithStatus._1.get

    assert(fact.predicate === "amd:foo")
    assert(fact.objectType === "s")
    assert(fact.objectValue === "bar, dance and café")
  }

}
