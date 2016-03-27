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
class CSV_EventReaderSuite extends FunSuite {

  def eventByResourceIterator(filename: String): EventByResourceIterator = {
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    eventByResourceReader(file)
  }

  test("Object EventsReader can read an empty CSV file") {
    assert(eventByResourceIterator("/empty_CSV_file.csv").isEmpty)
  }

  test("Object EventsReader can read a CSV file with a header line") {
    assert(eventByResourceIterator("/event_csv/header.csv").isEmpty)
  }

  test("Object EventsReader can read a CSV file with 1 data line") {
    assertResult(1)(eventByResourceIterator("/event_csv/one_data_line.csv").size)
  }

  test("Object EventsReader returns Resource and Event with 3 PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.get.subject.toString.length)
    assertResult(3)(eventByResource_0.event.get.pos.size)
  }

  test("Object EventsReader returns Resource and Event with all present PredicateObjects even if one is empty") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line_empty_entry.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.get.subject.toString.length)
    println(eventByResource_0.event.get.pos)
    assertResult(2)(eventByResource_0.event.get.pos.size)
  }

  test("Object EventsReader skips empty lines") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line_empty_entry.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val eventByResource_1: EventByResource = iterator.next()
    assertResult(36)(eventByResource_1.resource.get.subject.toString.length)
    println(eventByResource_1.event.get.pos)
    assertResult(3)(eventByResource_1.event.get.pos.size)
  }

  test("Object EventsReader returns Event with detailed PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val event = eventByResource_0.event.get
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

}
