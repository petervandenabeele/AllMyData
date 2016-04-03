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

  test("PredicateObject fails with empty object for an integer") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:ping", "", "i")
    }
  }

  test("PredicateObject fails with invalid object for an integer") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:ping", "abc", "i")
    }
  }

  test("PredicateObject fails with empty object for a decimal") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:dec", "", "d")
    }
  }

  test("PredicateObject fails with invalid object for a decimal") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:dec", "abc.def", "d")
    }
  }

  test("PredicateObject fails with empty object for a reference") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:ref", "", "r")
    }
  }

  test("PredicateObject fails with invalid object for a reference") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:ref", "abcd-too-short", "r")
    }
  }

  test("PredicateObject succeeds with a time that at least has proper date format") {
    PredicateObject("amd:time", "2016-04-03", "t")
    PredicateObject("amd:time", "2016-04-03T13:20:53Z", "t")
    PredicateObject("amd:time", "2016-04-03T13:20:53.123Z", "t")
    PredicateObject("amd:time", "2016-04-03T13:20:53.123456789Z", "t")
  }

  test("PredicateObject fails with invalid object for a time") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:time", "61-02-03", "t")
    }
  }

  test("PredicateObject succeeds with a uri (any format)") {
    PredicateObject("amd:uri", "", "u")
    PredicateObject("amd:uri", "http://foobar.org", "u")
    PredicateObject("amd:uri", "http://foo.org/bar?x=4&y=7", "u")
    PredicateObject("amd:uri", "a83hjcx$%#^&", "u")
  }

  test("PredicateObject fails with undefined objectType") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:xyz", "abc", "z")
    }
  }

  test("PredicateObject fails with a not yet implemented objectType") {
    intercept[IllegalArgumentException] {
      PredicateObject("amd:float", "1.56", "f")
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
