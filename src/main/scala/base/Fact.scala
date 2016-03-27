/**
  * Created by peter_v on 21/11/14.
  */

package base

import java.net.URL
import java.util.UUID
import java.time.{ZoneId, ZonedDateTime}

import cli.Util
import common._
import PredicateObject.validatePredicateObject

/** The core abstraction of this data model.
  *
  * The full log of all information is stores as an ordered Sequence of facts.
  * Each fact has an id, timestamp, context, a subject, predicate, object and objectType.
  */

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact(timeStamp: ATD_TimeStamp = ZonedDateTime.now(ZoneId.of("UTC")).toString,
                uuid: ATD_Uuid = UUID.randomUUID().toString,
                context: Context = Context(None),
                subject: ATD_Subject = newUUID,
                predicate: ATD_Predicate,
                objectType: ATD_ObjectType,
                objectValue: ATD_ObjectValue) {

  validatePredicateObject(
    predicate = predicate,
    objectValue = objectValue,
    objectType = objectType
  )

  override def toString: String = {
    List(
      timeStamp,
      uuid,
      context,
      subject,
      predicate,
      objectType,
      objectValue
    ).mkString(separator)
  }
}

object Fact {
  private val filename = "/predicates/valid_predicates.csv"
  private val resourceFile = getClass.getResource(filename)
  private val file =
    if (resourceFile.isInstanceOf[URL])
      scala.io.Source.fromURL(resourceFile)
    else
      scala.io.Source.fromFile(Util.getFullFilename("valid_predicates.csv", "metadata"))

  val validPredicates: Set[String] =
    file.getLines().filterNot(x => x.isEmpty).map(line => line.split(separator, 2)(0)).toSet
}
