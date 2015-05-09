/**
*  Created by peter_v on 04/05/15.
*/

package json

import base.{PredicateObject, EventByResource}
import common._
import json.JSON_EventReader.eventByResourceReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JSON_EventReaderSuite extends FunSuite {

  def eventByResourceIterator(schemaName: String, fileName: String): EventByResourceIterator = {
    val schema = scala.io.Source.fromURL(getClass.getResource(schemaName))
    val file = scala.io.Source.fromURL(getClass.getResource(fileName))
    eventByResourceReader(schema, file)
  }

  test("Object CSV_EventReader can read an empty JSON file") {
    assert(eventByResourceIterator("/event_json/schema1.json","/empty_JSON_file.json").isEmpty)
  }

  test("Object JSON_EventReader can read the first entry of a JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource.get
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.get.pos.head
    assertResult("atd:foo")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("bar")(predicateObject_0.objectValue)
  }

  test("Object JSON_EventReader can read two facts in one entry of a JSON file") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_json/schema1.json", "/event_json/foo_bar.json")

    val eventByResource_0: EventByResource = iterator.next()

    val resource_0 = eventByResource_0.resource.get
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.get.pos.head
    assertResult("atd:foo")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("bar")(predicateObject_0.objectValue)

    val predicateObject_1 = eventByResource_0.event.get.pos.tail.head
    assertResult("atd:bar")(predicateObject_1.predicate)
    assertResult("s")(predicateObject_1.objectType)
    assertResult("ping")(predicateObject_1.objectValue)
  }
}
