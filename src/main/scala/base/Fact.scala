/**
 * Created by peter_v on 21/11/14.
 */

package base

import java.net.URL
import java.util.UUID
import java.time.{ZoneId, ZonedDateTime}

import cli.Util
import common._

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact (timeStamp: ATD_TimeStamp = ZonedDateTime.now(ZoneId.of("UTC")).toString,
                 uuid: ATD_Uuid = UUID.randomUUID().toString,
                 context: Context = Context(None),
                 subject: ATD_Subject = newUUID(),
                 predicate: ATD_Predicate,
                 objectType: ATD_ObjectType,
                 objectValue: ATD_ObjectValue) {

  if(! Fact.validPredicates.contains(predicate))
    throw new IllegalArgumentException(s"The predicate $predicate is not in list of validPredicates")

  if(objectValue.isEmpty)
    throw new IllegalArgumentException(s"The objectValue for new Fact with predicate $predicate is empty")

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
  val filename = "/predicates/valid_predicates.csv"
  val resourceFile = getClass.getResource(filename)
  val file =
    if(resourceFile.isInstanceOf[URL])
      scala.io.Source.fromURL(resourceFile)
    else
      scala.io.Source.fromFile(Util.getFullFilename("valid_predicates.csv","metadata"))

  val validPredicates: Set[String] =
    file.getLines().filterNot(x => x.isEmpty).map(line => line.split(separator, 2)(0)).toSet
}
