/**
 * Created by peter_v on 04/03/15.
 */

package slick


import java.util.UUID

import base.Fact

import scala.slick.driver.H2Driver.simple._

object H2DB {

  def makeDb: Database = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

  def foo(db: Database): List[(Int, String)] = {
    val foos: TableQuery[Foos] = TableQuery[Foos]

    db.withSession { implicit session =>
      // Create the schema by combining the DDLs for the Suppliers and Coffees
      // tables using the query interfaces
      (foos.ddl).create

      foos +=(101, "foo is bar")
      foos +=(102, "ping is tux")

      foos.list
    }
  }

  def insert_fact(db: Database, fact: Fact): List[(String, String, Option[UUID], UUID, String, String, String)] = {
    val facts: TableQuery[FactsInDB] = TableQuery[FactsInDB]
    db.withSession { implicit session =>
      // Create the schema by combining the DDLs for the Suppliers and Coffees
      // tables using the query interfaces
      (facts.ddl).create

      val factInDB = (
        fact.timeStamp,
        fact.uuid,
        fact.context,
        fact.subject,
        fact.predicate,
        fact.objectType,
        fact.objectValue)

      facts += factInDB

      facts.list
    }
  }

  def read_facts(db: Database): List[(String)] = { //, String, String, String, String, String, String)] = {
    val facts: TableQuery[FactsInDB] = TableQuery[FactsInDB]

    val query = for (fact <- facts) yield fact.timeStamp
    val result = db.withSession {
      implicit session => {
        query.list
      }
    }
    result
  }
}
