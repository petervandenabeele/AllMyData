/**
 * Created by peter_v on 22/02/15.
 */

package csv

import csv.CSV_EventReader.eventByResourceReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CSV_EventReaderSuite extends FunSuite {

  test("Object CSV_EventReader can read an empty CSV file") {
    val filename = "/empty_CSV_file.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val eventByResourceIterator = eventByResourceReader(file)
    assert(eventByResourceIterator.isEmpty)
  }

  test("Object CSV_EventReader can read a CSV file with a header line") {
    val filename = "/event_csv/header.csv"
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    val eventByResourceIterator = eventByResourceReader(file)
    assert(eventByResourceIterator.isEmpty)
  }
}
