/**
  * Created by peter_v on 17/04/15.
  */

package json

import base.{PredicateObject, Event, Resource, EventByResource}
import common._
import org.json4s.JsonAST.JValue

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
      case JLong(_) => None
      case JInt(_) => None
      case JNothing => None
      case JNull => None
      case JString(_) => None
    }

    topList.get.map { case JObject(rawPos) =>
      EventByResource(
        // new resource, not trying to find existing
        resource = Some(Resource()),
        event = Some(Event(rawPos.map {
          case (rawPredicate, JString(objectValue)) => makePredicateObject(schemaJson, rawPredicate, objectValue)
          case (rawPredicate, JInt(objectValue)) => makePredicateObject(schemaJson, rawPredicate, objectValue.toString())
          case _ => PredicateObject(predicate = "amd:error", objectValue = "Found unsupported JSON type (only string and int)", objectType = "s")
        }))
      )
    }.toIterator
  }

  private def makePredicateObject(schemaJson: JValue, rawPredicate: String, objectValueString: String): PredicateObject = {
    val predicate: String = (schemaJson \ rawPredicate \ "predicate").values.toString
    val objectType: String = (schemaJson \ rawPredicate \ "objectType").values.toString
    PredicateObject(predicate = predicate, objectValue = objectValueString, objectType = objectType)
  }
}
