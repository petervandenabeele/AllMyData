/**
 * Created by peter_v on 04/03/15.
 */

package slick

import base.Fact

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
  }

  test("Slick + H2DB works") {
    new testFoo {
      assertResult(List((101, "foo is bar"),
        (102, "ping is tux")))(H2DB.foo(db))
    }
  }

  test("Insert a fact into the Facts table") {
    new testFoo {
      val results = H2DB.insert_fact(db, fact)
      assertResult(1)(results.size)
    }
  }

  test("Read a fact from the Facts table") {
    new testFoo {
      H2DB.read_facts(db).foreach {
        case (timeStamp) => println(timeStamp)
      }
    }
  }

}
