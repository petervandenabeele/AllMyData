/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EventByResourceSuite extends FunSuite {

  trait testEventByResource {
    val eventByResource = EventByResource(resource = None,
      event = None)
  }

  test("EventByResource can be created without explicit arguments") {
    new testEventByResource {
      assert(1 == 1)
    }
  }
}
