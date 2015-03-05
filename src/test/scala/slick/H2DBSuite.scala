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

  test("Slick + H2DB works") {
    assertResult(List((101, "foo is bar"),
                      (102, "ping is tux")))(H2DB.foo)
  }

  trait testFoo {
    val fact =
      Fact(predicate = "atd:foo",
        objectType = "s",
        objectValue = "Bar")
  }

  test("Insert a fact into Facts table") {
    new testFoo {
      H2DB.insert_fact(fact)
    }
  }
}
