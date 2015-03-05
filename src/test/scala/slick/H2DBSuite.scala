/**
 * Created by peter_v on 04/03/15.
 */

package slick

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class H2DBSuite extends FunSuite {

  test("Slick + H2DB works") {
    assertResult(List((101, "foo is bar"),
                      (102, "ping is tux")))(H2DB.foo)
  }
}
