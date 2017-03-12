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

  def eventByResourceIterator(schemaName: String, fileName: String, factsAtOption: Option[String] = None): EventByResourceIterator = {
    val schema = scala.io.Source.fromURL(getClass.getResource(schemaName))
    val file = scala.io.Source.fromURL(getClass.getResource(fileName))
    eventByResourceReader(schema, file, factsAtOption)
  }

  test("Object EventsReader can read an empty JSON eventFile") {
    assert(eventByResourceIterator("/event_json/schema1.json", "/empty_JSON_file.json").isEmpty)
  }

  test("Object JsonEventsReader can read the first entry of a JSON eventFile") {
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

  test("Object JsonEventsReader sets the at timestamp to a value set in context with amd:context:facts_at") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json", Some("2016-04-01"))

    val eventByResource_0: EventByResource = iterator.next()
    val resource_0 = eventByResource_0.resource
    val predicateObject_0 = eventByResource_0.event.pos.head
    assert(predicateObject_0.at.get.matches("""^2016-04-01$"""))
  }

  test("Object JsonEventsReader can read two entries in one event of a JSON eventFile") {
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

  test("Object JsonEventsReader can read two entries when in other order in JSON eventFile") {
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

  test("Object JsonEventsReader can two events in a JSON eventFile") {
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

  test("Object JsonEventsReader can read a boolean and write a \"b\" objectType") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema3.json", "/event_json/foo_bar_boolean.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource
    assertResult(36)(resource_0.subject.toString.length)

    val predicateObject_00 = eventByResource_0.event.pos.head
    assertResult("amd:foo")(predicateObject_00.predicate)
    assertResult("s")(predicateObject_00.objectType)
    assertResult("London")(predicateObject_00.objectValue)

    val predicateObject_01 = eventByResource_0.event.pos.tail.head
    assertResult("amd:boolean")(predicateObject_01.predicate)
    assertResult("b")(predicateObject_01.objectType)
    assertResult("true")(predicateObject_01.objectValue)


    val eventByResource_1: EventByResource = iterator.next()

    val resource_1 = eventByResource_1.resource
    assertResult(36)(resource_1.subject.toString.length)

    val predicateObject_10 = eventByResource_1.event.pos.head
    assertResult("amd:foo")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("SFO")(predicateObject_10.objectValue)

    val predicateObject_11 = eventByResource_1.event.pos.tail.head
    assertResult("amd:boolean")(predicateObject_11.predicate)
    assertResult("b")(predicateObject_11.objectType)
    assertResult("false")(predicateObject_11.objectValue)

    val eventByResource_2: EventByResource = iterator.next()

    val resource_2 = eventByResource_2.resource
    assertResult(36)(resource_2.subject.toString.length)

    val predicateObject_20 = eventByResource_2.event.pos.head
    assertResult("amd:foo")(predicateObject_20.predicate)
    assertResult("s")(predicateObject_20.objectType)
    assertResult("space")(predicateObject_20.objectValue)

    val predicateObject_21 = eventByResource_2.event.pos.tail.head
    assertResult("amd:error")(predicateObject_21.predicate)
    assertResult("s")(predicateObject_21.objectType)
    assertResult("amd:boolean must be 'true' or 'false'")(predicateObject_21.objectValue)

    val eventByResource_3: EventByResource = iterator.next()

    val resource_3 = eventByResource_3.resource
    assertResult(36)(resource_3.subject.toString.length)

    val predicateObject_30 = eventByResource_3.event.pos.head
    assertResult("amd:foo")(predicateObject_30.predicate)
    assertResult("s")(predicateObject_30.objectType)
    assertResult("Paris")(predicateObject_30.objectValue)

    val predicateObject_31 = eventByResource_3.event.pos.tail.head
    assertResult("amd:boolean")(predicateObject_31.predicate)
    assertResult("b")(predicateObject_31.objectType)
    assertResult("true")(predicateObject_31.objectValue)


    val eventByResource_4: EventByResource = iterator.next()

    val resource_4 = eventByResource_4.resource
    assertResult(36)(resource_4.subject.toString.length)

    val predicateObject_40 = eventByResource_4.event.pos.head
    assertResult("amd:foo")(predicateObject_40.predicate)
    assertResult("s")(predicateObject_40.objectType)
    assertResult("NYC")(predicateObject_40.objectValue)

    val predicateObject_41 = eventByResource_4.event.pos.tail.head
    assertResult("amd:boolean")(predicateObject_41.predicate)
    assertResult("b")(predicateObject_41.objectType)
    assertResult("false")(predicateObject_41.objectValue)
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

  test("Object JsonEventsReader does not crash on unsupported value type and read amd:dec") {
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
    assertResult("The objectType f is not in supported List(s, t, u, r, i, d, b)")(predicateObject_01.objectValue)

    val predicateObject_02 = eventByResource_0.event.pos.tail.tail.head
    assertResult("amd:error")(predicateObject_02.predicate)
    assertResult("s")(predicateObject_02.objectType)
    assertResult("Found unsupported JSON type (only string, int, decimal and boolean); type is JNull$")(predicateObject_02.objectValue)

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
