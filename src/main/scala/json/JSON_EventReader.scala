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
      "bar" -> Map[String, String]("predicate" -> "atd:bar", "objectType" -> "s")
    )

    topList.get.map(entry => {
      val rawPos = entry match {
        case JObject(list) => list
      }

      val rawPredicates: Seq[String] = rawPos.map(rawPo => rawPo._1)
      val objectValues: Seq[String] =  rawPos.map(rawPo => rawPo._2 match {
        case JString(s) => s
        case _ => ""
      })

      val predicateObjects =
        rawPredicates.
          zip(objectValues).
          map { case (rawPredicate, objectValue) =>
            val predicate = schema(rawPredicate)("predicate")
            val objectType = schema(rawPredicate)("objectType")
            PredicateObject(predicate = predicate,
              objectType = objectType,
              objectValue = objectValue)
        }
      EventByResource(resource = Some(Resource()),
        event = Some(Event(predicateObjects)))
    }).toIterator
  }
}
