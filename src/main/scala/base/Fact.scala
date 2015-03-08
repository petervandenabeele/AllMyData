/**
 * Created by peter_v on 21/11/14.
 */

package base

import java.util.UUID
import java.time.{ZoneId, ZonedDateTime}

import common._

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact (timeStamp: ATD_TimeStamp = ZonedDateTime.now(ZoneId.of("UTC")).toString,
                 uuid: ATD_Uuid = UUID.randomUUID().toString,
                 context: ATD_Context = "",
                 subject: ATD_Subject = newUUID(),
                 predicate: ATD_Predicate,
                 objectType: ATD_ObjectType,
                 objectValue: ATD_ObjectValue) {

  val filename = "/predicates/legal_predicates.csv"
  val file = scala.io.Source.fromURL(getClass.getResource(filename))
  val legalPredicates: Set[String] =
    file.getLines().map(line => line.split(",", 1)(0)).toSet

  if(!legalPredicates.contains(predicate))
    throw new IllegalArgumentException(s"The predicate $predicate is not in list of legalPredicates")
}
