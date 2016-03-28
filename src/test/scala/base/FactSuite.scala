/**
  * Created by peter_v on 20/11/14.
  */

package base

import common._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  trait testFoo {
    val fact =
      Fact(predicate = "amd:foo",
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
      val predicate: AMD_Predicate = fact.predicate
      assert(predicate === "amd:foo")
    }
  }

  test("Fact has an objectType attribute") {
    new testFoo {
      val objectType: AMD_ObjectType = fact.objectType
      assert(objectType === "s")
    }
  }

  test("Fact has an objectValue attribute") {
    new testFoo {
      val objectValue: AMD_ObjectValue = fact.objectValue
      assert(objectValue === "Bar")
    }
  }

  test("Fact has a UUID subject") {
    new testFoo {
      val testSubject = newUUID
      val testSubjectString = testSubject.toString
      val factWithSubject = fact.copy(subject = testSubject)
      val subject: AMD_Subject = factWithSubject.subject
      assert(subject === testSubject)
    }
  }

  test("Fact has a default arg for subject and default is a UUID") {
    new testFoo {
      val subject: AMD_Subject = fact.subject
      assert(subject.toString.length === 36)
    }
  }

  test("Fact has an optional uuid") {
    new testFoo {
      val factWithUuid = fact.copy(uuid = newUUID.toString)
      val uuid: AMD_Uuid = factWithUuid.uuid
      assert(uuid.size === 36)
    }
  }

  test("Fact has an optional uuid and default is a new random UUID") {
    new testFoo {
      val uuid: AMD_Uuid = fact.uuid
      assert(uuid.size === 36)
    }
  }

  test("Fact has an optional timeStamp") {
    new testFoo {
      val factWithTimeStamp = fact.copy(timeStamp = "2014-11-21 23:59:36.134567890")
      val timeStamp: AMD_TimeStamp = factWithTimeStamp.timeStamp
      assert(timeStamp === "2014-11-21 23:59:36.134567890")
    }
  }

  test("Fact has an optional timeStamp and default is a UTC time") {
    new testFoo {
      val timeStamp: AMD_TimeStamp = fact.timeStamp
      assert(timeStamp.size === 29)
    }
  }

  test("Fact has an optional context") {
    new testFoo {
      val factWithContext = fact.copy(context = Context(Some(newUUID)))
      val context: Context = factWithContext.context
      assert(context.toString.length === 36)
    }
  }

  test("Fact has an optional context and default is an empty string") {
    new testFoo {
      val context: Context = fact.context
      assert(context == Context(None))
    }
  }

  test("Fact only accepts predicates from a list") {
    intercept[IllegalArgumentException] {
      Fact(predicate = "amd:foobar", // not valid
        objectType = "s",
        objectValue = "Bar")
    }
  }

  test("Fact cannot have empty objectValue when not a string") {
    intercept[IllegalArgumentException] {
      Fact(predicate = "amd:ping", // not valid
        objectType = "i",
        objectValue = "")
    }
  }

  test("Fact can be created with empty objectValue when a string") {
    Fact(predicate = "amd:foo",
      objectType = "s",
      objectValue = "")
  }
}
