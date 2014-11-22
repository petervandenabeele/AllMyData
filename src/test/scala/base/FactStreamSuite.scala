/**
 * Created by peter_v on 22/11/14.
 */

package base

import common._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactStreamSuite extends FunSuite {

  trait testPeter {
    val fact1 =
      Fact(predicate = "atd:first_name",
           objectType = "s",
           objectValue = "Peter")
    val fact2 =
      Fact(predicate = "atd:last_name",
           objectType = "s",
           objectValue = "V")
  }

  test("Empty FactStream can be created") {
    new testPeter {
      val factStream: FactStream = Stream.empty
      assert(factStream.size === 0)
   }
  }

  test("FactStream with 2 facts can be created") {
    new testPeter {
      val factStream: FactStream = fact2 #:: fact1 #:: Stream.empty
      assert(factStream.size === 2)
      assert(factStream.head.objectValue === "V")
      assert(factStream.tail.head.objectValue === "Peter")
    }
  }
}
