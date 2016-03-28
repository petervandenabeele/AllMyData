/**
  * Created by peter_v on 22/02/15.
  */

package csv

import base.{PredicateObject, EventByResource}
import common._
import csv.EventsReader.eventByResourceReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EventsReaderSuite extends FunSuite {

  def eventByResourceIterator(filename: String): EventByResourceIterator = {
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    eventByResourceReader(file)
  }

  test("Object EventsReader can read an empty CSV file") {
    assert(eventByResourceIterator("/empty_CSV_file.csv").isEmpty)
  }

  test("Object EventsReader can read a CSV file with header lines") {
    assert(eventByResourceIterator("/event_csv/header.csv").isEmpty)
  }

  test("Object EventsReader can read a CSV file with 1 data line") {
    assertResult(1)(eventByResourceIterator("/event_csv/one_data_line.csv").size)
  }

  test("Object EventsReader returns Resource and Event with 3 PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.subject.toString.length)
    assertResult(3)(eventByResource_0.event.pos.size)
  }

  test("Object EventsReader returns Resource and Event with all present PredicateObjects even if one is empty") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line_empty_entry.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.subject.toString.length)
    assertResult(2)(eventByResource_0.event.pos.size)
  }

  test("Object EventsReader skips empty lines") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line_empty_entry.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val eventByResource_1: EventByResource = iterator.next()
    assertResult(36)(eventByResource_1.resource.subject.toString.length)
    assertResult(3)(eventByResource_1.event.pos.size)
  }

  test("Object EventsReader returns Event with detailed PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val event = eventByResource_0.event
    val predicateObject: PredicateObject = event.pos.head
    assertResult("amd:bar")(predicateObject.predicate)
    assertResult("s")(predicateObject.objectType)
    assertResult("Bar")(predicateObject.objectValue)
    val nextPredicateObject: PredicateObject = event.pos.tail.head
    assertResult("amd:foo")(nextPredicateObject.predicate)
    assertResult("s")(nextPredicateObject.objectType)
    assertResult("Foo")(nextPredicateObject.objectValue)
    val nextNextPredicateObject: PredicateObject = event.pos.tail.tail.head
    assertResult("amd:ping")(nextNextPredicateObject.predicate)
    assertResult("i")(nextNextPredicateObject.objectType)
    assertResult(42)(nextNextPredicateObject.objectValue.toInt) // TODO return a real Int for "i" objectType
  }

  test("Object EventsReader returns Events for 2 resources") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/two_events.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val event = eventByResource_0.event
    val predicateObject: PredicateObject = event.pos.head
    assertResult("amd:bar")(predicateObject.predicate)
    assertResult("s")(predicateObject.objectType)
    assertResult("Bar")(predicateObject.objectValue)
    val nextPredicateObject: PredicateObject = event.pos.tail.head
    assertResult("amd:foo")(nextPredicateObject.predicate)
    assertResult("s")(nextPredicateObject.objectType)
    assertResult("Foo")(nextPredicateObject.objectValue)
    val nextNextPredicateObject: PredicateObject = event.pos.tail.tail.head
    assertResult("amd:ping")(nextNextPredicateObject.predicate)
    assertResult("i")(nextNextPredicateObject.objectType)
    assertResult(42)(nextNextPredicateObject.objectValue.toInt) // TODO return a real Int for "i" objectType

    val eventByResource_1: EventByResource = iterator.next()
    val event_1 = eventByResource_1.event
    val predicateObject_1: PredicateObject = event_1.pos.head
    assertResult("amd:bar")(predicateObject_1.predicate)
    assertResult("s")(predicateObject_1.objectType)
    assertResult("Ping")(predicateObject_1.objectValue)
    val nextPredicateObject_1: PredicateObject = event_1.pos.tail.head
    assertResult("amd:foo")(nextPredicateObject_1.predicate)
    assertResult("s")(nextPredicateObject_1.objectType)
    assertResult("Pong")(nextPredicateObject_1.objectValue)
    val nextNextPredicateObject_1: PredicateObject = event_1.pos.tail.tail.head
    assertResult("amd:ping")(nextNextPredicateObject_1.predicate)
    assertResult("i")(nextNextPredicateObject_1.objectType)
    assertResult(37)(nextNextPredicateObject_1.objectValue.toInt) // TODO return a real Int for "i" objectType
  }

}
