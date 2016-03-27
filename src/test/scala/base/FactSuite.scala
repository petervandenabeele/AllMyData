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
      val predicate: ATD_Predicate = fact.predicate
      assert(predicate === "amd:foo")
    }
  }

  test("Fact has an objectType attribute") {
    new testFoo {
      val objectType: ATD_ObjectType = fact.objectType
      assert(objectType === "s")
    }
  }

  test("Fact has an objectValue attribute") {
    new testFoo {
      val objectValue: ATD_ObjectValue = fact.objectValue
      assert(objectValue === "Bar")
    }
  }

  test("Fact has a UUID subject") {
    new testFoo {
      val testSubject = newUUID()
      val testSubjectString = testSubject.toString
      val factWithSubject = fact.copy(subject = testSubject)
      val subject: ATD_Subject = factWithSubject.subject
      assert(subject === testSubject)
    }
  }

  test("Fact has a default arg for subject and default is a UUID") {
    new testFoo {
      val subject: ATD_Subject = fact.subject
      assert(subject.toString.size === 36)
    }
  }

  test("Fact has an optional uuid") {
    new testFoo {
      val factWithUuid = fact.copy(uuid = newUUID().toString)
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

  test("Fact has an optional timeStamp and default is a UTC time") {
    new testFoo {
      val timeStamp: ATD_TimeStamp = fact.timeStamp
      assert(timeStamp.size === 29)
    }
  }

  test("Fact has an optional context") {
    new testFoo {
      val factWithContext = fact.copy(context = Context(Some(newUUID)))
      val context: Context = factWithContext.context
      assert(context.toString.size === 36)
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

  test("Fact cannot have empty objectValue") {
    intercept[IllegalArgumentException] {
      Fact(predicate = "amd:foo", // not valid
        objectType = "s",
        objectValue = "")
    }
  }
}
