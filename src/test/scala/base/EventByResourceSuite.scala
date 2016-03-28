/**
  * Created by peter_v on 22/02/15.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EventByResourceSuite extends FunSuite {

  test("EventByResource can be created with a resource and an event") {
    val eventByResource = EventByResource(resource = Resource(), event = Event() )
  }

}
