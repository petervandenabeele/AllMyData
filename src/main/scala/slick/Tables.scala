/**
 * Created by peter_v on 04/03/15.
 */

package slick

import java.time.{ZoneId, ZonedDateTime}
import java.util.UUID

import slick.lifted.ProvenShape
import slick.driver.H2Driver.api._

class Foos(tag: Tag) extends Table[(Int, String)](tag, "FOOS") {
  // This is the primary key column:
  def id: Rep[Int] = column[Int]("FOO_ID", O.PrimaryKey)
  def name: Rep[String] = column[String]("FOO_NAME")

  def * : ProvenShape[(Int, String)] = (id, name)
}

class FactsInDB(tag: Tag) extends Table[(String, String, Option[UUID], UUID, String, String, String)](tag, "FACTS") {
  // This is the primary key column:
  def timeStamp: Rep[String] = column[String]("FACT_TIMESTAMP", O.PrimaryKey)
  def uuid: Rep[String] = column[String]("FACT_UUID")
  def context: Rep[Option[UUID]] = column[Option[UUID]]("FACT_CONTEXT", O.SqlType("UUID"))
  def subject: Rep[UUID] = column[UUID]("FACT_SUBJECT", O.SqlType("UUID"))
  def predicate: Rep[String] = column[String]("FACT_PREDICATE")
  def objectType: Rep[String] = column[String]("FACT_OBJECT_TYPE")
  def objectValue: Rep[String] = column[String]("FACT_OBJECT_VALUE")

  def * : ProvenShape[(String, String, Option[UUID], UUID, String, String, String)] = {
    (timeStamp, uuid, context, subject, predicate, objectType, objectValue)
  }
}
