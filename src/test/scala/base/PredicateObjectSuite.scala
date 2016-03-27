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

  test("PredicateObject can be create with empty object when a string") {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = ""
    )
    assertResult(PredicateObject("amd:bar", "", "s")) { testPredicateObject }
  }
}
