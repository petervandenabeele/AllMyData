/**
 * Created by peter_v on 22/02/15.
 */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EventSuite extends FunSuite {

  trait testEvent {
    val event = Event()
  }

  test("Event can be created without the default arguments") {
    new testEvent {
      assert(event == Event())
    }
  }
}
