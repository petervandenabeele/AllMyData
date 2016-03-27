/**
 * Created by peter_v on 04/03/15.
 */

package slick


import java.util.UUID

import base.Fact

import slick.driver.H2Driver.api._

object H2DB {

  def makeDb: Database = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

  val foos: TableQuery[Foos] = TableQuery[Foos]

//  def makeFoosTable(db: Database): Unit = {
//    db.withSession { implicit session =>
//      (foos.ddl).create
//    }
//  }
//
  val facts: TableQuery[FactsInDB] = TableQuery[FactsInDB]

//  def makeFactsTable(db: Database): Unit = {
//    db.withSession { implicit session =>
//      // Create the schema by combining the DDLs for the Suppliers and Coffees
//      // tables using the query interfaces
//      (facts.ddl).create
//    }
//  }
//

//  def insert_fact(db: Database, fact: Fact): List[(String, String, Option[UUID], UUID, String, String, String)] = {
//    db.withSession { implicit session =>
//      // TODO context.getValue
//      val factInDB = (
//        fact.timeStamp,
//        fact.uuid,
//        fact.context.context,
//        fact.subject,
//        fact.predicate,
//        fact.objectType,
//        fact.objectValue)
//
//      facts += factInDB
//
//      facts.list
//    }
//  }

//  def read_facts(db: Database):  List[(String, String, Option[UUID], UUID, String, String, String)] = {
//
//    val query = for (fact <- facts) yield fact
//
//    val result = db.withSession {
//      implicit session => {
//        query.list
//      }
//    }
//    result
//  }
}
