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

  trait testFooBar {
    val fact1 =
      Fact(predicate = "atd:foo",
           objectType = "s",
           objectValue = "Bar")
    val fact2 =
      Fact(predicate = "atd:tux",
           objectType = "s",
           objectValue = "Ping")
  }

  test("Empty FactStream can be created") {
    new testFooBar {
      val factStream: FactStream = Stream.empty
      assert(factStream.size === 0)
   }
  }

  test("FactStream with 2 facts can be created") {
    new testFooBar {
      val factStream: FactStream = fact2 #:: fact1 #:: Stream.empty
      assert(factStream.size === 2)
      assert(factStream.head.objectValue === "Ping")
      assert(factStream.tail.head.objectValue === "Bar")
    }
  }
}
