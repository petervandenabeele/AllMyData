/**
  * Created by peter_v on 17/04/15.
  */

package json

import base.EventByResource._
import base._
import common._
import org.json4s.JsonAST.JValue

import scala.io.BufferedSource
import org.json4s._
import org.json4s.native.JsonMethods._

object JsonEventsReader {

  /** Read the actual facts and print them */
  def reader(file: BufferedSource, contextOption: Option[Context] = None, schemaFile: Option[BufferedSource] = None): FactWithStatusIterator = {
    val eventByResourceIterator = eventByResourceReader(schemaFile.get, file)

    eventByResourceIterator.flatMap[FactWithStatus](eventByResource => {
      factsFromEventByResource(eventByResource, contextOption.get).map(fact => (Some(fact), None))
    })
  }

  def eventByResourceReader(schemaFile: BufferedSource, eventFile: BufferedSource): EventByResourceIterator = {
    // parse the schema first
    val schemaString = schemaFile.mkString
    val schemaJson = parse(schemaString)

    // parse the core json
    // FIXME : proper streaming
    val eventString = eventFile.mkString
    val eventJson = parse(eventString, useBigDecimalForDouble = true)

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
        resource = Resource(),
        event = Event(rawPos.map {
          case (rawPredicate, JString(objectValue)) => makePredicateObject(schemaJson, rawPredicate, objectValue)
          case (rawPredicate, JInt(objectValue)) => makePredicateObject(schemaJson, rawPredicate, objectValue.toString())
          case (rawPredicate, JDecimal(objectValue)) => makePredicateObject(schemaJson, rawPredicate, objectValue.toString)
          case other => PredicateObject(
            predicate = "amd:error",
            objectValue = s"Found unsupported JSON type (only string, int and decimal); type is ${other._2.getClass.getSimpleName}",
            objectType = "s")
        })
      )
    }.toIterator
  }

  private def makePredicateObject(schemaJson: JValue, rawPredicate: String, objectValueString: String): PredicateObject = {
    val predicate: String = (schemaJson \ rawPredicate \ "predicate").values.toString
    val objectType: String = (schemaJson \ rawPredicate \ "objectType").values.toString
    try {
      PredicateObject(
        predicate = predicate,
        objectValue = objectValueString,
        objectType = objectType
      )
    } catch {
      case e: java.lang.IllegalArgumentException =>
        PredicateObject.errorPredicateObject(e.getMessage)
    }
  }
}
