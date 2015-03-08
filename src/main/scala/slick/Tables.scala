/**
 * Created by peter_v on 04/03/15.
 */

package slick

import java.time.{ZoneId, ZonedDateTime}
import java.util.UUID

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ProvenShape}

class Foos(tag: Tag) extends Table[(Int, String)](tag, "FOOS") {
  // This is the primary key column:
  def id: Column[Int] = column[Int]("FOO_ID", O.PrimaryKey)
  def name: Column[String] = column[String]("FOO_NAME")

  def * : ProvenShape[(Int, String)] = (id, name)
}

class FactsInDB(tag: Tag) extends Table[(String, String, String, String, String, String, String)](tag, "FACTS") {
  // This is the primary key column:
  def timeStamp: Column[String] = column[String]("FACT_TIMESTAMP", O.PrimaryKey)
  def uuid: Column[String] = column[String]("FACT_UUID")
  def context: Column[String] = column[String]("FACT_CONTEXT")
//  def subject: Column[UUID] = column[UUID]("FACT_SUBJECT")
  def predicate: Column[String] = column[String]("FACT_PREDICATE")
  def subject: Column[String] = column[String]("FACT_SUBJECT")
  def objectType: Column[String] = column[String]("FACT_OBJECT_TYPE")
  def objectValue: Column[String] = column[String]("FACT_OBJECT_VALUE")

//  def * : ProvenShape[(String, String, String, UUID, String, String, String)] = {
  def * : ProvenShape[(String, String, String, String, String, String, String)] = {
    (timeStamp, uuid, context, subject, predicate, objectType, objectValue)
  }
}
