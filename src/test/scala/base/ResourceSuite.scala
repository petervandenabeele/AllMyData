/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResourceSuite extends FunSuite {

  test("Resource can be created without explicits") {
    Resource() // does not fail
  }
}
