/**
  * Created by peter_v on 21/11/14.
  */

package base

import java.net.URL
import java.time.{ZoneId, ZonedDateTime}

import cli.Util
import common._

/** The core abstraction of this data model. The full log of all information is stored as
  * an ordered Sequence of facts. Each fact has:
  *
  * id, timestamp, context, subject, predicate, objectType, objectValue, and
  * optional `at`, `from`, `to` timestamps (`from` and `to` are inclusive)
  *
  * Construction can only occur from a predicateObject (that is the real "data" part with validations).
  *
  * TODO Fix the timestamp to have more digits and/or be monotonic.
  */

case class Fact(timestamp: AMD_Timestamp = Fact.now,
                id: AMD_Id = newUUID,
                context: Context = Context(None),
                subject: AMD_Subject = newUUID,
                predicateObject: PredicateObject) {

  def predicate: AMD_Predicate = predicateObject.predicate

  def objectType: AMD_ObjectType = predicateObject.objectType

  def objectValue: AMD_ObjectValue = predicateObject.objectValue

  def at: OptionalTimestamp = predicateObject.at

  def from: OptionalTimestamp = predicateObject.from

  def to: OptionalTimestamp = predicateObject.to

  override def toString: String = {
    List(
      List(
        timestamp,
        id,
        context,
        subject
      ).mkString(separator),
      predicateObject.toString
    ).mkString(separator)
  }
}

object Fact {
  /** drop "[UTC]" */
  def now: String = ZonedDateTime.now(ZoneId.of("UTC")).toString.takeWhile(c => c != '[')

  /** drop the "THH:MM:SS.mmmZ" part */
  def today: String = now.takeWhile(c => c != 'T')

  val validPredicates: Set[String] = {
    val resourceName = "/predicates/valid_predicates.csv"
    val resourceFile = getClass.getResource(resourceName)
    val file =
      if (resourceFile.isInstanceOf[URL])
        scala.io.Source.fromURL(resourceFile)
      else
        scala.io.Source.fromFile(Util.getFullFilename("metadata/valid_predicates.csv"))

    file.getLines().filterNot(x => x.isEmpty).map(line => line.split(separator, 2)(0)).toSet
  }
}
