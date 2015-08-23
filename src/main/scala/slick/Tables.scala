/**
 * Created by peter_v on 04/03/15.
 */

package slick

import java.time.{ZoneId, ZonedDateTime}
import java.util.UUID

import slick.lifted.ProvenShape

import scala.slick.driver.H2Driver.simple._

class Foos(tag: Tag) extends Table[(Int, String)](tag, "FOOS") {
  // This is the primary key column:
  def id: Column[Int] = column[Int]("FOO_ID", O.PrimaryKey)
  def name: Column[String] = column[String]("FOO_NAME")

  def * : ProvenShape[(Int, String)] = (id, name)
}

class FactsInDB(tag: Tag) extends Table[(String, String, Option[UUID], UUID, String, String, String)](tag, "FACTS") {
  // This is the primary key column:
  def timeStamp: Column[String] = column[String]("FACT_TIMESTAMP", O.PrimaryKey)
  def uuid: Column[String] = column[String]("FACT_UUID")
  def context: Column[Option[UUID]] = column[Option[UUID]]("FACT_CONTEXT", O.DBType("UUID"))
  def subject: Column[UUID] = column[UUID]("FACT_SUBJECT", O.DBType("UUID"))
  def predicate: Column[String] = column[String]("FACT_PREDICATE")
  def objectType: Column[String] = column[String]("FACT_OBJECT_TYPE")
  def objectValue: Column[String] = column[String]("FACT_OBJECT_VALUE")

  def * : ProvenShape[(String, String, Option[UUID], UUID, String, String, String)] = {
    (timeStamp, uuid, context, subject, predicate, objectType, objectValue)
  }
}
