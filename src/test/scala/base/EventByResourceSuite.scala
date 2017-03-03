/**
  * Created by peter_v on 22/02/15.
  */

package base

import common._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EventByResourceSuite extends FunSuite {

  test("EventByResource can be created with a resource and an event") {
    EventByResource(resource = Resource(), event = Event() )
  }

  trait testEventByResource {
    val contextNone = Context(None)
    val contextSome = Context(Some(newUUID))
    val eventByResource = EventByResource(
      resource = Resource(),
      event = Event(pos = Seq(
        PredicateObject(
          predicate = "amd:foo",
          objectValue = "Foo",
          objectType = "s"),
        PredicateObject(
          predicate = "amd:bar",
          objectValue = "Bar",
          objectType = "s")
      ))
    )
  }

  test("factsFromEventByResource can be created with empty context") {
    new testEventByResource {
      EventByResource.factsFromEventByResource(
        eventByResource = eventByResource,
        context = contextNone)
    }
  }

  test("factsFromEventByResource are correct with actual context") {
    new testEventByResource {
      val facts: Seq[Fact] = EventByResource.factsFromEventByResource(
        eventByResource = eventByResource,
        context = contextSome)
      // order is important
      val fact_0: Fact = facts.head
      val fact_1: Fact = facts.tail.head
      assertResult(contextSome){ fact_0.context }
      assertResult("amd:foo"){ fact_0.predicate }
      assertResult("Foo"){ fact_0.objectValue }
      assertResult("s"){ fact_0.objectType }
      assertResult(contextSome){ fact_1.context }
      assertResult("amd:bar"){ fact_1.predicate }
      assertResult("Bar"){ fact_1.objectValue }
      assertResult("s"){ fact_1.objectType }
    }
  }

}
