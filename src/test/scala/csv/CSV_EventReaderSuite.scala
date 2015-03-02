/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.EventByResource
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

  test("Object CSV_EventReader returns Resource and Event with 1 PredicateObject") {
    val eventByResource: EventByResource =
      eventByResourceIterator("/event_csv/one_data_line.csv").next()
    assertResult(36)(eventByResource.resource.get.subject.size)
    assertResult(1)(eventByResource.event.get.pos.size)
  }
}
