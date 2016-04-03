/**
  * Created by peter_v on 21/11/14.
  */

package base

import java.net.URL
import java.time.{ZoneId, ZonedDateTime}

import cli.Util
import common._

/** The core abstraction of this data model.
  *
  * The full log of all information is stored as an ordered Sequence of facts.
  *
  * Each fact has id, timestamp, context, subject, predicate, objectType, objectValue.
  *
  * Construction can only occur from a predicateObject (that is the real "data" part with validations).
  */

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact(timeStamp: AMD_TimeStamp = ZonedDateTime.now(ZoneId.of("UTC")).toString,
                id: AMD_Id = newUUID,
                context: Context = Context(None),
                subject: AMD_Subject = newUUID,
                predicateObject: PredicateObject) {

  def predicate = predicateObject.predicate
  def objectType = predicateObject.objectType
  def objectValue = predicateObject.objectValue

  override def toString: String = {
    List(
      timeStamp,
      id,
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
