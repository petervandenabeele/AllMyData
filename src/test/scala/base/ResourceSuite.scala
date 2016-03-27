/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResourceSuite extends FunSuite {

  trait testResource {
    val resource = Resource()
  }

  test("Resource can be created without explicits") {
    new testResource {
      assert(1 == 1)
    }
  }
}
