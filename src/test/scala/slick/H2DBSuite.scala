/**
 * Created by peter_v on 04/03/15.
 */

package slick

import base.{Context, Fact}
import common._

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class H2DBSuite extends FunSuite {

  trait testFoo {
    val db = H2DB.makeDb
    val fact =
      Fact(predicate = "atd:foo",
        objectType = "s",
        objectValue = "Bar")
    val factWithContext =
      Fact(context = Context(newUUID().toString),
        predicate = "atd:foo",
        objectType = "s",
        objectValue = "Foo")
  }

  test("Slick + H2DB works") {
    new testFoo {
      H2DB.makeFoosTable(db)
      assertResult(List(
        (101, "foo is bar"),
        (102, "ping is tux"))) (H2DB.foo(db))
    }
  }

  test("Insert a fact into the Facts table") {
    new testFoo {
      H2DB.makeFactsTable(db)
      val results = H2DB.insert_fact(db, fact)
      assertResult(1)(results.size)
    }
  }

  test("Read a fact from the Facts table") {
    new testFoo {
      H2DB.read_facts(db).foreach {
        case (timeStamp,
              uuid,
              context,
              subject,
              predicate,
              objectType,
              objectValue) => {
          assertResult(29)(timeStamp.toString.size)
          assertResult(36)(uuid.toString.size)
          assertResult(None)(context)
          assertResult(36)(subject.toString.size)
          assertResult("atd:foo")(predicate.toString)
          assertResult("s")(objectType.toString)
          assertResult("Bar")(objectValue.toString)
        }
      }
    }
  }

  test("Read a fact with a context from the Facts table") {
    new testFoo {
      val results = H2DB.insert_fact(db, factWithContext)
      H2DB.read_facts(db).drop(1).foreach {
        case (timeStamp,
        uuid,
        context,
        subject,
        predicate,
        objectType,
        objectValue) => {
          assertResult(29)(timeStamp.toString.size)
          assertResult(36)(uuid.toString.size)
          assertResult(36)(context.get.toString.size)
          assertResult(36)(subject.toString.size)
          assertResult("atd:foo")(predicate.toString)
          assertResult("s")(objectType.toString)
          assertResult("Foo")(objectValue.toString)
        }
      }
    }
  }
}
