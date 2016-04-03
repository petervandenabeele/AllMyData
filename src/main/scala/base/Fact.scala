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
  * Each fact has id, timestamp, context, subject, predicate, objectType, objectValue, and
  * optional `at`, `from`, `to` timestamps (`from` and `to` are inclusive)
  *
  * Construction can only occur from a predicateObject (that is the real "data" part with validations).
  */

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact(timestamp: AMD_Timestamp = Fact.now,
                id: AMD_Id = newUUID,
                context: Context = Context(None),
                subject: AMD_Subject = newUUID,
                predicateObject: PredicateObject) {

  def predicate = predicateObject.predicate
  def objectType = predicateObject.objectType
  def objectValue = predicateObject.objectValue
  def at   = predicateObject.at
  def from = predicateObject.from
  def to   = predicateObject.to

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
  private def now = ZonedDateTime.now(ZoneId.of("UTC")).toString.dropRight(5) // drop `[UTC]`
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
