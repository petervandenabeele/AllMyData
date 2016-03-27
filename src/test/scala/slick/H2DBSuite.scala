/**
 * Created by peter_v on 04/03/15.
 */

package slick

import base.{Context, Fact}
import common._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import slick.dbio.DBIO
import slick.driver.H2Driver.api._

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
      val setup = DBIO.seq(
        // Create the tables, including primary and foreign keys
        (H2DB.foos.schema ++ H2DB.facts.schema).create,

        // Insert some foos
        H2DB.foos +=(101, "foo is bar"),
        H2DB.foos +=(102, "ping is tux")
      )

      val setupFuture = db.run(setup)
    }
  }

  test("Insert a fact into the Facts table") {
    new testFoo {
      // TODO context.getValue
      val factInDB = (
        fact.timeStamp,
        fact.uuid,
        fact.context.context,
        fact.subject,
        fact.predicate,
        fact.objectType,
        fact.objectValue
      )

      val setup = DBIO.seq(
        // Create the facts table
        H2DB.facts.schema.create,

        // Insert a fact
        H2DB.facts += factInDB
      )

      val setupFuture = db.run(setup)
    }
  }

//  test("Read a fact from the Facts table") {
//    new testFoo {
//      H2DB.read_facts(db).foreach {
//        case (timeStamp,
//              uuid,
//              context,
//              subject,
//              predicate,
//              objectType,
//              objectValue) => {
//          assertResult(29)(timeStamp.toString.size)
//          assertResult(36)(uuid.toString.size)
//          assertResult(None)(context)
//          assertResult(36)(subject.toString.size)
//          assertResult("atd:foo")(predicate.toString)
//          assertResult("s")(objectType.toString)
//          assertResult("Bar")(objectValue.toString)
//        }
//      }
//    }
//  }
//
//  test("Read a fact with a context from the Facts table") {
//    new testFoo {
//      val results = H2DB.insert_fact(db, factWithContext)
//      H2DB.read_facts(db).drop(1).foreach {
//        case (timeStamp,
//        uuid,
//        context,
//        subject,
//        predicate,
//        objectType,
//        objectValue) => {
//          assertResult(29)(timeStamp.toString.size)
//          assertResult(36)(uuid.toString.size)
//          assertResult(36)(context.get.toString.size)
//          assertResult(36)(subject.toString.size)
//          assertResult("atd:foo")(predicate.toString)
//          assertResult("s")(objectType.toString)
//          assertResult("Foo")(objectValue.toString)
//        }
//      }
//    }
//  }
}
