package base

/**
 * Created by peter_v on 20/11/14.
 */

import common._
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  trait testPeter {
    val fact =
      Fact(predicate = "atd:first_name",
           objectType = "s",
           objectValue = "Peter")
  }

  test("Fact can be created without the default arguments") {
    new testPeter {
      assert(fact.objectValue != "") // does not fail
    }
  }

  test("Fact has a predicate attribute") {
    new testPeter {
      val predicate: ATD_Predicate = fact.predicate
      assert(predicate === "atd:first_name")
    }
  }

  test("Fact has an object_type attribute") {
    new testPeter {
      val objectType: ATD_ObjectType = fact.objectType
      assert(objectType === "s")
    }
  }

  test("Fact has an object attribute") {
    new testPeter {
      val objectValue: ATD_ObjectValue = fact.objectValue
      assert(objectValue === "Peter")
    }
  }

  test("Fact has an optional subject") {
    new testPeter {
      val factWithSubject = fact.copy(subject = "abcd-1234")
      val subject: ATD_Subject = factWithSubject.subject
      assert(subject === "abcd-1234")
    }
  }
}

