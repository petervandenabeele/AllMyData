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

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class H2DBSuite extends FunSuite {

  trait testFoo {
    val db = H2DB.makeDb
    val fact =
      Fact(predicate = "amd:foo",
        objectType = "s",
        objectValue = "Bar")
    val factWithContext =
      Fact(context = Context(newUUID().toString),
        predicate = "amd:foo",
        objectType = "s",
        objectValue = "Foo")
    def factTuple(fact: Fact) = (
      fact.timeStamp,
      fact.uuid,
      fact.context.context,
      fact.subject,
      fact.predicate,
      fact.objectType,
      fact.objectValue
      )
  }

  test("Slick + H2DB works and CREATES TABLE FOOS and FACTS") {
    new testFoo {
      val setup = DBIO.seq(
        // Create the foos tables
        H2DB.foos.schema.create,

        // Insert some foos
        H2DB.foos +=(101, "foo is bar"),
        H2DB.foos +=(102, "ping is tux")
      )

      val setupFuture = db.run(setup)
      setupFuture.onComplete {
        case Success(()) => // println(s"test 1: success creating the tables")
        case Failure(t) => // do nothing, will be trapped by Await
      }
      Await.result(setupFuture, 1000.millis)
    }
  }

  test("Insert a fact into the Facts table") {
    new testFoo {
      // TODO context.getValue
      val setup = DBIO.seq(
        // Create the facts table
        H2DB.facts.schema.create,

        // Insert a fact
        H2DB.facts += factTuple(fact)
      )

      val setupFuture = db.run(setup)

      setupFuture.onComplete {
        case Success(()) => // println(s"test 2: success inserting a tuple")
        case Failure(t) => // do nothing, will be trapped by Await
      }
      Await.result(setupFuture, 1000.millis)
    }
  }

  test("Read a fact from the Facts table") {
    new testFoo {
      val results = db.run(H2DB.facts.result)

      val testResult = results.map(r => {
        r.foreach {
          case (
            timeStamp,
            uuid,
            context,
            subject,
            predicate,
            objectType,
            objectValue) => {
            // println(s"test 3A: asserting accuracy of the tuple")
            assertResult(29)(timeStamp.toString.length)
            assertResult(36)(uuid.toString.length)
            assertResult(None)(context)
            assertResult(36)(subject.toString.length)
            assertResult("amd:foo")(predicate.toString)
            assertResult("s")(objectType.toString)
            assertResult("Bar")(objectValue.toString)
          }
        }
        r
      })

      testResult.onComplete {
        case Success(completedResults) => {
          // println(s"test 3B: success reading tuples $completedResults")
          assertResult(1){ completedResults.length }
        }
        case Failure(t) => // do nothing, will be trapped by Await
      }

      Await.result(testResult, 1000.millis)
    }
  }

  test("Insert and read an additional fact with a context from the Facts table") {
    new testFoo {

      val setup = DBIO.seq(
        // Insert a fact
        H2DB.facts += factTuple(factWithContext)
      )

      val setupFuture = db.run(setup)

      val results = db.run(H2DB.facts.result)

      val testResult = results.map(r => {
        r.map {
          case (
            timeStamp,
            uuid,
            context,
            subject,
            predicate,
            objectType,
            objectValue) => {
            // println(s"test 4A: objectValue is $objectValue")
            objectValue
          }
        }.sorted.mkString(",")
      })

      testResult.onComplete {
        case Success(completedResults) => {
          // println(s"test 4B: success reading objectValues $completedResults")
          assertResult("Bar,Foo") { completedResults }
        }
        case Failure(t) => // do nothing, will be trapped by Await
      }

      Await.result(testResult, 1000.millis)
    }
  }
}
