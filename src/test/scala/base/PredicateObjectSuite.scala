/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PredicateObjectSuite extends FunSuite {

  trait testPredicateObject {
    val predicateObject = PredicateObject()
  }

  test("PredicateObject can be created without explicit arguments") {
    new testPredicateObject {
      assert(predicateObject == PredicateObject())
    }
  }
}
