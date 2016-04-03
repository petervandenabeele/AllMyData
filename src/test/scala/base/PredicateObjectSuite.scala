/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PredicateObjectSuite extends FunSuite {

  trait testPredicateObject {
    val predicateObject = PredicateObject(
      predicate = "rdf:type",
      objectValue = "Foo"
    )
  }

  test("PredicateObject can be created without explicit objectType") {
    new testPredicateObject {
      assertResult(PredicateObject("rdf:type", "Foo", "s")) { predicateObject }
    }
  }

  test("PredicateObject fails with empty predicate") {
    intercept[IllegalArgumentException] {
      PredicateObject("", "Foo", "s")
    }
  }

  test("PredicateObject fails with empty object when not a string") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:ping", "", "i")
    }
  }

  test("PredicateObject can be created with empty object when a string") {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = ""
    )
    assertResult(PredicateObject("amd:bar", "", "s")) { testPredicateObject }
  }

  test("PredicateObject can be created with at, from, to timestamps") {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = "",
      at   = OptionalTimestamp("2014-11-21T23:59:36.123456789Z"),
      from = OptionalTimestamp("2013-01-01T00:00:00Z"),
      to   = OptionalTimestamp("2015-12-31T23:59:59.999Z")
    )
    assertResult("2014-11-21T23:59:36.123456789Z") { testPredicateObject.at.toString }
    assertResult("2013-01-01T00:00:00Z") { testPredicateObject.from.toString }
    assertResult("2015-12-31T23:59:59.999Z") { testPredicateObject.to.toString }
  }

  test("PredicateObject can be created with at timestamp") {
    PredicateObject(
      predicate = "amd:bar",
      objectValue = "",
      at   = OptionalTimestamp("2014-11-21T23:59:36.123456789Z")
    )
  }

  test("PredicateObject can be created with from timestamp") {
    PredicateObject(
      predicate = "amd:bar",
      objectValue = "",
      from = OptionalTimestamp("2014-11-21T23:59:36.123456789Z")
    )
  }

  test("PredicateObject can be created with to timestamp") {
    PredicateObject(
      predicate = "amd:bar",
      objectValue = "",
      to   = OptionalTimestamp("2014-11-21T23:59:36.123456789Z")
    )
  }

  test("PredicateObject is invalid when can be created with to earlier than from timestamps") {
    intercept[IllegalArgumentException] {
      val testPredicateObject = PredicateObject(
        predicate = "amd:bar",
        objectValue = "",
        from = OptionalTimestamp("2016-01-01T00:00:00Z"),
        to = OptionalTimestamp("2015-12-31T23:59:59.999Z")
      )
    }
  }

  test("toString returns CSV style result") {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = "bar",
      at   = OptionalTimestamp("2014-11-21T23:59:36.123456789Z"),
      from = OptionalTimestamp("2013-01-01T00:00:00Z"),
      to   = OptionalTimestamp("2015-12-31T23:59:59.999Z")
    )
    val expected = "amd:bar;s;bar;2014-11-21T23:59:36.123456789Z;2013-01-01T00:00:00Z;2015-12-31T23:59:59.999Z"
    assertResult(expected){ testPredicateObject.toString }
  }

  test("toString returns CSV style result with empty timestamps") {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = "bar"
    )
    val expected = "amd:bar;s;bar;;;"
    assertResult(expected){ testPredicateObject.toString }
  }

}
