/**
  * Created by peter_v on 04/05/15.
  */

package json

import base.EventByResource
import common._
import json.JsonEventsReader.eventByResourceReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonEventsReaderSuite extends FunSuite {

  def eventByResourceIterator(schemaName: String, fileName: String): EventByResourceIterator = {
    val schema = scala.io.Source.fromURL(getClass.getResource(schemaName))
    val file = scala.io.Source.fromURL(getClass.getResource(fileName))
    eventByResourceReader(schema, file)
  }

  test("Object EventsReader can read an empty JSON file") {
    assert(eventByResourceIterator("/event_json/schema1.json", "/empty_JSON_file.json").isEmpty)
  }

  test("Object JsonEventsReader can read the first entry of a JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("bar")(predicateObject_0.objectValue)
  }

  test("Object JsonEventsReader sets the at timestamp") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()
    val resource_0 = eventByResource_0.resource
    val predicateObject_0 = eventByResource_0.event.pos.head
    assert(predicateObject_0.at.get.matches("""^\d{4}-\d\d-\d\d$"""))
  }

    test("Object JsonEventsReader can read two entries in one event of a JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("bar")(predicateObject_0.objectValue)

    val predicateObject_1 = eventByResource_0.event.pos.tail.head
    assertResult("amd:bar")(predicateObject_1.predicate)
    assertResult("s")(predicateObject_1.objectType)
    assertResult("ping")(predicateObject_1.objectValue)
  }

  test("Object JsonEventsReader can read two entries when in other order in JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/bar_foo.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.pos.head
    assertResult("amd:bar")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("ping")(predicateObject_0.objectValue)

    val predicateObject_1 = eventByResource_0.event.pos.tail.head
    assertResult("amd:foo")(predicateObject_1.predicate)
    assertResult("s")(predicateObject_1.objectType)
    assertResult("bar")(predicateObject_1.objectValue)
  }

  test("Object JsonEventsReader can two events in a JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)

    val predicateObject_00 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_00.predicate)
    assertResult("s")(predicateObject_00.objectType)
    assertResult("bar")(predicateObject_00.objectValue)

    val predicateObject_01 = eventByResource_0.event.pos.tail.head
    assertResult("amd:bar")(predicateObject_01.predicate)
    assertResult("s")(predicateObject_01.objectType)
    assertResult("ping")(predicateObject_01.objectValue)


    val eventByResource_1: EventByResource = iterator.next()

    val resource_1 = eventByResource_1.resource
    assertResult(36)(resource_1.subject.toString.length)

    val predicateObject_10 = eventByResource_1.event.pos.head
    assertResult("amd:foo")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("tux")(predicateObject_10.objectValue)

    val predicateObject_11 = eventByResource_1.event.pos.tail.head
    assertResult("amd:bar")(predicateObject_11.predicate)
    assertResult("s")(predicateObject_11.objectType)
    assertResult("pong")(predicateObject_11.objectValue)
  }

  test("Object JsonEventsReader can read an Int and write an \"i\" objectType") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema2.json", "/event_json/foo_bar_int.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)

    val predicateObject_00 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_00.predicate)
    assertResult("s")(predicateObject_00.objectType)
    assertResult("London")(predicateObject_00.objectValue)

    val predicateObject_01 = eventByResource_0.event.pos.tail.head
    assertResult("amd:int")(predicateObject_01.predicate)
    assertResult("i")(predicateObject_01.objectType)
    assertResult("442")(predicateObject_01.objectValue)


    val eventByResource_1: EventByResource = iterator.next()

    val resource_1 = eventByResource_1.resource
    assertResult(36)(resource_1.subject.toString.length)

    val predicateObject_10 = eventByResource_1.event.pos.head
    assertResult("amd:foo")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("SFO")(predicateObject_10.objectValue)

    val predicateObject_11 = eventByResource_1.event.pos.tail.head
    assertResult("amd:int")(predicateObject_11.predicate)
    assertResult("i")(predicateObject_11.objectType)
    assertResult("537")(predicateObject_11.objectValue)
  }

  test("Object JsonEventsReader uses the schema2.json info") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema2.json", "/event_json/foo_bar_int.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)

    val predicateObject_00 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_00.predicate)
    assertResult("s")(predicateObject_00.objectType)
    assertResult("London")(predicateObject_00.objectValue)

    val predicateObject_01 = eventByResource_0.event.pos.tail.head
    assertResult("amd:int")(predicateObject_01.predicate)
    assertResult("i")(predicateObject_01.objectType)
    assertResult("442")(predicateObject_01.objectValue)


    val eventByResource_1: EventByResource = iterator.next()

    val resource_1 = eventByResource_1.resource
    assertResult(36)(resource_1.subject.toString.length)

    val predicateObject_10 = eventByResource_1.event.pos.head
    assertResult("amd:foo")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("SFO")(predicateObject_10.objectValue)

    val predicateObject_11 = eventByResource_1.event.pos.tail.head
    assertResult("amd:int")(predicateObject_11.predicate)
    assertResult("i")(predicateObject_11.objectType)
    assertResult("537")(predicateObject_11.objectValue)
  }

  test("Object JsonEventsReader does not crash on unsupported value type") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema2.json", "/event_json/foo_bar_int_with_float.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)

    val predicateObject_00 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_00.predicate)
    assertResult("s")(predicateObject_00.objectType)
    assertResult("London")(predicateObject_00.objectValue)

    val predicateObject_01 = eventByResource_0.event.pos.tail.head
    assertResult("amd:error")(predicateObject_01.predicate)
    assertResult("s")(predicateObject_01.objectType)
    assertResult("The objectType f is not in supported List(s, t, u, r, i, d)")(predicateObject_01.objectValue)

    val predicateObject_02 = eventByResource_0.event.pos.tail.tail.head
    assertResult("amd:error")(predicateObject_02.predicate)
    assertResult("s")(predicateObject_02.objectType)
    assertResult("Found unsupported JSON type (only string, int and decimal); type is JBool")(predicateObject_02.objectValue)

    val predicateObject_03 = eventByResource_0.event.pos.tail.tail.tail.head
    assertResult("amd:dec")(predicateObject_03.predicate)
    assertResult("d")(predicateObject_03.objectType)
    assertResult("22.65")(predicateObject_03.objectValue)


    val eventByResource_1: EventByResource = iterator.next()

    val resource_1 = eventByResource_1.resource
    assertResult(36)(resource_1.subject.toString.length)

    val predicateObject_10 = eventByResource_1.event.pos.head
    assertResult("amd:foo")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("SFO")(predicateObject_10.objectValue)

    val predicateObject_11 = eventByResource_1.event.pos.tail.head
    assertResult("amd:int")(predicateObject_11.predicate)
    assertResult("i")(predicateObject_11.objectType)
    assertResult("537")(predicateObject_11.objectValue)
  }
}
