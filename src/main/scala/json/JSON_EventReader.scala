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

    val schema = Map[String, Map[String,String]](
      "foo" -> Map[String, String]("predicate" -> "atd:foo", "objectType" -> "s"),
      "bar" -> Map[String, String]("predicate" -> "atd:bar", "objectType" -> "s"),
      "city" -> Map[String, String]("predicate" -> "my:city", "objectType" -> "s"),
      "bars" -> Map[String, String]("predicate" -> "my:bars", "objectType" -> "i")
    )

    topList.get.map { case JObject(rawPos) =>
      EventByResource(
        // new resource, not trying to find existing
        resource = Some(Resource()),
        event = Some(Event(rawPos.map {
          case (rawPredicate, JString(objectValue)) =>
            PredicateObject(
              predicate = schema(rawPredicate)("predicate"),
              objectType = schema(rawPredicate)("objectType"),
              objectValue = objectValue)
          case (rawPredicate, JInt(objectValue)) =>
            PredicateObject(
              predicate = schema(rawPredicate)("predicate"),
              objectType = schema(rawPredicate)("objectType"),
              objectValue = objectValue.toString())
          case _ => PredicateObject() // Empty PredicateObject
        }))
      )
    }.toIterator
  }
}
