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

  def eventByResourceIterator(schemaName: String, fileName: String): EventByResourceIterator = {
    val schema = scala.io.Source.fromURL(getClass.getResource(schemaName))
    val file = scala.io.Source.fromURL(getClass.getResource(fileName))
    eventByResourceReader(schema, file)
  }

  test("Object CSV_EventReader can read an empty CSV file") {
    assert(eventByResourceIterator("/event_json/schema1.json","/empty_JSON_file.json").isEmpty)
  }
}
