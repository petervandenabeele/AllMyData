/**
  * Created by peter_v on 20/11/14.
  */

package base

import java.io.ByteArrayInputStream
import scala.io.BufferedSource
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers._

import common._
import csv.FactsReader

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  trait testFoo {
    val predicateObject =
      PredicateObject(
        predicate = "amd:foo",
        objectType = "s",
        objectValue = "Bar")
    val fact =
      Fact(predicateObject = predicateObject)
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
      val factWithId = fact.copy(id = newUUID)
      val id: AMD_Id = factWithId.id
      assert(id.isInstanceOf[AMD_Id])
    }
  }

  test("Fact has an optional uuid and default is a new random UUID") {
    new testFoo {
      val id: AMD_Id = fact.id
      assert(id.isInstanceOf[AMD_Id])
    }
  }

  test("Fact has an optional timeStamp") {
    new testFoo {
      val factWithTimestamp = fact.copy(timestamp = "2014-11-21T23:59:36.123456789Z")
      val timestamp: AMD_Timestamp = factWithTimestamp.timestamp
      assert(timestamp === "2014-11-21T23:59:36.123456789Z")
    }
  }

  test("Fact has an optional timeStamp and default is a UTC time") {
    new testFoo {
      val timestamp: AMD_Timestamp = fact.timestamp
      println(timestamp)
      assert(timestamp.toString.length === 24) // 3 fractionals a Z and no [UTC]
      assert(timestamp.startsWith("20"))
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
      val predicateObject = PredicateObject(
        predicate = "amd:foobar", // not valid
        objectType = "s",
        objectValue = "Bar"
      )
      Fact(predicateObject = predicateObject) // code never reached
    }
  }

  test("Fact cannot have empty objectValue when not a string") {
    intercept[IllegalArgumentException] {
      val predicateObject = PredicateObject(
        predicate = "amd:ping",
        objectType = "i",
        objectValue = ""
      )
      Fact(predicateObject = predicateObject) // code never reached
    }
  }

  test("Fact can be created with empty objectValue when a string") {
    val predicateObject = PredicateObject(
      predicate = "amd:foo",
      objectType = "s",
      objectValue = ""
    )
    Fact(predicateObject = predicateObject)
  }

  trait PredicateObjectWithTimestamps {
    val testPredicateObject = PredicateObject(
      predicate = "amd:bar",
      objectValue = "bar",
      at   = OptionalTimestamp("2014-11-21T23:59:36.123456789Z"),
      from = OptionalTimestamp("2013-01-01T00:00:00Z"),
      to   = OptionalTimestamp("2015-12-31T23:59:59.999Z")
    )
  }

  test("Fact can be created with at, from, to timestamps") {
    new PredicateObjectWithTimestamps {
      Fact(predicateObject = testPredicateObject)
    }
  }

  test("The at, from, to timestamps are available as getters") {
    new PredicateObjectWithTimestamps {
      val fact = Fact(predicateObject = testPredicateObject)
      fact.toString should include regex "2014-11-21T23:59:36.123456789Z"
      fact.toString should include regex "2013-01-01T00:00:00Z"
      fact.toString should include regex "2015-12-31T23:59:59.999Z"
    }
  }

  test("The at, from, to timestamps are shown in toString") {
    new PredicateObjectWithTimestamps {
      val fact = Fact(predicateObject = testPredicateObject)
      assertResult("2014-11-21T23:59:36.123456789Z") { fact.at.get }
      assertResult("2013-01-01T00:00:00Z") { fact.from.get }
      assertResult("2015-12-31T23:59:59.999Z") { fact.to.get }
    }
  }

  test("[Integration] The fact follows a general regex") {
    new PredicateObjectWithTimestamps {
      val fact = Fact(
        context = Context(Some(newUUID)),
        predicateObject = testPredicateObject
      )
      val hex = "[0-9a-f]"
      val uuidRegex = s"$hex{8}-$hex{4}-$hex{4}-$hex{4}-$hex{12}"
      val d2 = """\d{2}"""
      val d3 = """\d{3}"""
      val d4 = """\d{4}"""
      val re = s"""$d4-$d2-${d2}T$d2:$d2:$d2.${d3}Z;$uuidRegex;$uuidRegex;$uuidRegex;amd:bar;s;bar;2014-11-21T23:59:36.123456789Z;2013-01-01T00:00:00Z;2015-12-31T23:59:59.999Z"""
      fact.toString should fullyMatch regex re
    }
  }

  test("[Integration] A fact saved to facts file can be read back") {
    new PredicateObjectWithTimestamps {
      val fact = Fact(
        context = Context(Some(newUUID)),
        predicateObject = testPredicateObject
      )
      val serializedFact = fact.toString
      val reader = FactsReader.reader(new BufferedSource(new ByteArrayInputStream(serializedFact.getBytes)))
      val readFact: Fact = reader.take(1).next._1.get

      assertResult(fact.timestamp){ readFact.timestamp }
      assertResult(fact.id){ readFact.id }
      assertResult(fact.context){ readFact.context }
      assertResult(fact.subject){ readFact.subject }
      assertResult(fact.predicate){ readFact.predicate }
      assertResult(fact.objectType){ readFact.objectType }
      assertResult(fact.at){ readFact.at }
      assertResult(fact.from){ readFact.from }
      assertResult(fact.to){ readFact.to }
    }
  }
}
