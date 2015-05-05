/**
*  Created by peter_v on 17/04/15.
*/

package json

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

import org.json4s._
import org.json4s.native.JsonMethods._

object JSON_EventReader {

  def eventByResourceReader(schemaFile: BufferedSource, eventFile: BufferedSource): EventByResourceIterator = {
    // parse the schema first
    val schemaString = schemaFile.mkString
    val schemaJson = parse(schemaString)
    println(schemaJson)

    // parse the core json
    // FIXME : proper streaming
    val eventString = eventFile.mkString
    val eventJson = parse(eventString)

    val topList = eventJson match {
      case JObject(list) => Some(list)
      case JArray(list) => Some(list)
      // all other types not supported at this level
      case JBool(_) => None
      case JDecimal(_) => None
      case JDouble(_) => None
      case JInt(_) => None
      case JNothing => None
      case JNull => None
      case JString(_) => None
    }

    // println(topList.get)

    val predicates = List("atd:foo", "atd:bar")
    val objectTypes = List("s", "s")

    topList.get.map(entry => {
      val objectValues = List("bar")
      val predicateObjects =
        predicates.
          zip(objectTypes).
          zip(objectValues).
          map { case ((predicate, objectType), objectValue) =>
          PredicateObject(predicate = predicate,
            objectType = objectType,
            objectValue = objectValue)
        }
      EventByResource(resource = Some(Resource()),
        event = Some(Event(predicateObjects)))
    }).toIterator
  }
}
