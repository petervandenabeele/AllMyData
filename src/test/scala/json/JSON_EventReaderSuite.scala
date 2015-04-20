/**
 * Created by peter_v on 17/04/15.
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

  def eventByResourceIterator(filename: String): EventByResourceIterator = {
    val file = scala.io.Source.fromURL(getClass.getResource(filename))
    eventByResourceReader(file)
  }

  test("Object CSV_EventReader can read an empty CSV file") {
    assert(eventByResourceIterator("/empty_JSON_file.json").isEmpty)
  }
}
