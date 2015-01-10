/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact
import common.{FactIterator, FactWithStatus}
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
    val factIterator: FactIterator = reader(file)
    val factWithStatus: FactWithStatus = factIterator.next()
    val fact = factWithStatus._1.get

    assert(fact.predicate === "atd:foo")
    assert(fact.objectType === "s")
    assert(fact.objectValue === "bar and café")
  }

  test("one_fact.csv creates 3 facts for one resource") {
    val filename = "/one_fact.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator: FactIterator = reader(file)
    val facts: Array[Fact] = factIterator.map(p => p._1.get).toArray
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

    val resource_subject = fact_1.subject
    assert(fact_2.subject === resource_subject)
    assert(fact_3.subject === resource_subject)
  }

  test("one_fact_with_context.csv creates 1 context and 3 facts") {
    val filename = "/one_fact_with_context.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator: FactIterator = reader(file)
    val facts: Array[Fact] = factIterator.map(p => p._1.get).toArray
    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)
    val fact_3 = facts(4)

    assert(facts.size === 5)

    assert(context_1.predicate === "atd:context:source")
    assert(context_1.objectType === "s")
    assert(context_1.objectValue === "me")

    assert(context_2.predicate === "atd:context:ingress_time")
    assert(context_2.objectType === "s")
    assert(context_2.objectValue === "2014-12-12T22:13:00")

    val context_subject = context_1.subject
    assert(context_2.subject === context_subject)

    assert(fact_1.context === context_subject)
    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === context_subject)
    assert(fact_2.predicate === "atd:tux")
    assert(fact_2.objectType === "s")
    assert(fact_2.objectValue === "ping")

    assert(fact_3.context === context_subject)
    assert(fact_3.predicate === "atd:ping")
    assert(fact_3.objectType === "s")
    assert(fact_3.objectValue === "pong")

    val resource_subject = fact_1.subject
    assert(fact_2.subject === resource_subject)
    assert(fact_3.subject === resource_subject)
  }

  test("two_facts_with_context.csv 2 contexts for 2 facts") {
    val filename = "/two_facts_with_context.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator: FactIterator = reader(file)
    val facts: Array[Fact] = factIterator.map(p => p._1.get).toArray

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)

    assert(facts.size === 4)

    val context_subject_1 = context_1.subject
    val context_subject_2 = context_2.subject

    assert(fact_1.context === context_subject_1)
    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === context_subject_2)
    assert(fact_2.predicate === "atd:bar")
    assert(fact_2.objectType === "s")
    assert(fact_2.objectValue === "foo")
  }

  test("two_facts_with_csv_reference.csv links object first resource") {
    val filename = "/two_facts_with_csv_reference.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val factIterator: FactIterator = reader(file)
    val facts: Array[Fact] = factIterator.map(p => p._1.get).toArray

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)
    val fact_2 = facts(3)

    assert(facts.size === 4)

    val context_subject_1 = context_1.subject
    val context_subject_2 = context_2.subject

    assert(fact_1.context === context_subject_1)
    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    assert(fact_2.context === context_subject_2)
    assert(fact_2.predicate === "atd:bar")
    assert(fact_2.objectType === "r")
    assert(fact_2.objectValue === fact_1.subject)
  }

  test("two_facts_with_invalid_csv_reference.csv results in a failure for the second fact") {
    val filename = "/two_facts_with_invalid_csv_reference.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))

    val factIterator: FactIterator = reader(file)
    val factsWithStatusses = factIterator.toArray
    val facts: Array[Fact] = factsWithStatusses.filter(p => p._1.nonEmpty).map(p => p._1.get)

    val context_1 = facts(0)
    val context_2 = facts(1)
    val fact_1 = facts(2)

    val context_subject_1 = context_1.subject

    assert(fact_1.context === context_subject_1)
    assert(fact_1.predicate === "atd:foo")
    assert(fact_1.objectType === "s")
    assert(fact_1.objectValue === "bar")

    val errors: Array[String] = factsWithStatusses.filter(p => p._1.isEmpty).map(p => p._2.get)

    assert(errors.size === 1)

    val error_1 = errors(0)

    assert(error_1 === "csvObjectValue 15 could not be found")
  }
}
