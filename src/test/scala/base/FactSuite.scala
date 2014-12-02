/**
 * Created by peter_v on 20/11/14.
 */

package base

import java.util.UUID

import common._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  trait testFoo {
    val fact =
      Fact(predicate = "atd:foo",
           objectType = "s",
           objectValue = "Bar")
  }

  test("Fact can be created without the default arguments") {
    new testFoo {
      assert(fact.objectValue != "") // does not fail
    }
  }

  test("Fact has a predicate attribute") {
    new testFoo {
      val predicate: ATD_Predicate = fact.predicate
      assert(predicate === "atd:foo")
    }
  }

  test("Fact has an object_type attribute") {
    new testFoo {
      val objectType: ATD_ObjectType = fact.objectType
      assert(objectType === "s")
    }
  }

  test("Fact has an object attribute") {
    new testFoo {
      val objectValue: ATD_ObjectValue = fact.objectValue
      assert(objectValue === "Bar")
    }
  }

  test("Fact has an optional subject") {
    new testFoo {
      val factWithSubject = fact.copy(subject = "abcd-1234")
      val subject: ATD_Subject = factWithSubject.subject
      assert(subject === "abcd-1234")
    }
  }

  test("Fact has an optional subject and default is empty string") {
    new testFoo {
      val subject: ATD_Subject = fact.subject
      assert(subject === "")
    }
  }

  test("Fact has an optional uuid") {
    new testFoo {
      val factWithUuid = fact.copy(uuid = UUID.randomUUID().toString)
      val uuid: ATD_Uuid = factWithUuid.uuid
      assert(uuid.size === 36)
    }
  }

  test("Fact has an optional uuid and default is a new random UUID") {
    new testFoo {
      val uuid: ATD_Uuid = fact.uuid
      assert(uuid.size === 36)
    }
  }

  test("Fact has an optional timeStamp") {
    new testFoo {
      val factWithTimeStamp = fact.copy(timeStamp = "2014-11-21 23:59:36.134567890")
      val timeStamp: ATD_TimeStamp = factWithTimeStamp.timeStamp
      assert(timeStamp === "2014-11-21 23:59:36.134567890")
    }
  }

  test("Fact has an optional timeStamp and default is empty string") {
    new testFoo {
      val timeStamp: ATD_TimeStamp = fact.timeStamp
      assert(timeStamp === "")
    }
  }
}

