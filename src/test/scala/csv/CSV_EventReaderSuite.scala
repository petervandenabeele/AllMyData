/**
*  Created by peter_v on 22/02/15.
*/

package csv

import base.{PredicateObject, EventByResource}
import common._
import csv.CSV_EventReader.eventByResourceReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CSV_EventReaderSuite extends FunSuite {

  def eventByResourceIterator(filename: String): EventByResourceIterator = {
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    eventByResourceReader(file)
  }

  test("Object CSV_EventReader can read an empty CSV file") {
    assert(eventByResourceIterator("/empty_CSV_file.csv").isEmpty)
  }

  test("Object CSV_EventReader can read a CSV file with a header line") {
    assert(eventByResourceIterator("/event_csv/header.csv").isEmpty)
  }

  test("Object CSV_EventReader can read a CSV file with 1 data line") {
    assertResult(1)(eventByResourceIterator("/event_csv/one_data_line.csv").size)
  }

  test("Object CSV_EventReader returns Resource and Event with 3 PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.get.subject.toString.length)
    assertResult(3)(eventByResource_0.event.get.pos.size)
  }

  test("Object CSV_EventReader returns Resource and Event with all present PredicateObjects even if one is empty") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line_empty_entry.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assertResult(36)(eventByResource_0.resource.get.subject.toString.length)
    println(eventByResource_0.event.get.pos)
    assertResult(2)(eventByResource_0.event.get.pos.size)
  }

  test("Object CSV_EventReader returns Event with detailed PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/one_data_line.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val event = eventByResource_0.event.get
    val predicateObject: PredicateObject = event.pos.head
    assertResult("schema:givenName")(predicateObject.predicate)
    assertResult("s")(predicateObject.objectType)
    assertResult("Peter")(predicateObject.objectValue)
    val nextPredicateObject: PredicateObject = event.pos.tail.head
    assertResult("schema:familyName")(nextPredicateObject.predicate)
    assertResult("s")(nextPredicateObject.objectType)
    assertResult("Vandenabeele")(nextPredicateObject.objectValue)
  }

  test("Object CSV_EventReader returns more Events with detailed PredicateObjects") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/three_data_lines.csv")
    val eventByResource_0: EventByResource = iterator.next()
    val eventByResource_1: EventByResource = iterator.next()
    val eventByResource_2: EventByResource = iterator.next()
    assert(! iterator.hasNext)

    val resource_0 = eventByResource_0.resource.get
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.get.pos.head
    assertResult("schema:givenName")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("Peter")(predicateObject_0.objectValue)
    val predicateObject_1 = eventByResource_0.event.get.pos.tail.head
    assertResult("schema:familyName")(predicateObject_1.predicate)
    assertResult("s")(predicateObject_1.objectType)
    assertResult("Vandenabeele")(predicateObject_1.objectValue)
    val predicateObject_2 = eventByResource_0.event.get.pos.tail.tail.head
    assertResult("schema:email")(predicateObject_2.predicate)
    assertResult("s")(predicateObject_2.objectType)
    assertResult("p@v.net")(predicateObject_2.objectValue)

    val resource_1 = eventByResource_1.resource.get
    assertResult(36)(resource_1.subject.toString.length)
    val predicateObject_10 = eventByResource_1.event.get.pos.head
    assertResult("schema:givenName")(predicateObject_10.predicate)
    assertResult("s")(predicateObject_10.objectType)
    assertResult("Jan")(predicateObject_10.objectValue)
    val predicateObject_11 = eventByResource_1.event.get.pos.tail.head
    assertResult("schema:familyName")(predicateObject_11.predicate)
    assertResult("s")(predicateObject_11.objectType)
    assertResult("De Lange")(predicateObject_11.objectValue)
    val predicateObject_12 = eventByResource_1.event.get.pos.tail.tail.head
    assertResult("schema:email")(predicateObject_12.predicate)
    assertResult("s")(predicateObject_12.objectType)
    assertResult("j@dl.com")(predicateObject_12.objectValue)

    val resource_2 = eventByResource_2.resource.get
    assertResult(36)(resource_2.subject.toString.length)
    val predicateObject_20 = eventByResource_2.event.get.pos.head
    assertResult("schema:givenName")(predicateObject_20.predicate)
    assertResult("s")(predicateObject_20.objectType)
    assertResult("Piet")(predicateObject_20.objectValue)
    val predicateObject_21 = eventByResource_2.event.get.pos.tail.head
    assertResult("schema:familyName")(predicateObject_21.predicate)
    assertResult("s")(predicateObject_21.objectType)
    assertResult("De Korte")(predicateObject_21.objectValue)
    val predicateObject_22 = eventByResource_2.event.get.pos.tail.tail.head
    assertResult("schema:email")(predicateObject_22.predicate)
    assertResult("s")(predicateObject_22.objectType)
    assertResult("p@dk.org")(predicateObject_22.objectValue)
  }

  test("Object CSV_EventReader reads the objectTypes") {
    val iterator: EventByResourceIterator = eventByResourceIterator("/event_csv/with_object_type.csv")
    val eventByResource_0: EventByResource = iterator.next()
    assert(!iterator.hasNext)

    val resource_0 = eventByResource_0.resource.get
    assertResult(36)(resource_0.subject.toString.length)
    val predicateObject_0 = eventByResource_0.event.get.pos.head
    assertResult("schema:name")(predicateObject_0.predicate)
    assertResult("s")(predicateObject_0.objectType)
    assertResult("JrubyConf.eu 2013")(predicateObject_0.objectValue)

    val predicateObject_1 = eventByResource_0.event.get.pos.tail.head
    assertResult("schema:startDate")(predicateObject_1.predicate)
    assertResult("t")(predicateObject_1.objectType)
    assertResult("2013-08-14")(predicateObject_1.objectValue)
    val predicateObject_2 = eventByResource_0.event.get.pos.tail.tail.head
    assertResult("schema:endDate")(predicateObject_2.predicate)
    assertResult("t")(predicateObject_2.objectType)
    assertResult("2013-08-15")(predicateObject_2.objectValue)
  }
}
