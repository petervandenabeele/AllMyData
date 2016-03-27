/**
  * Created by peter_v on 04/03/15.
  */

package slick


import java.util.UUID

import base.Fact

import slick.driver.H2Driver.api._

object H2DB {

  def makeDb: Database = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

  val foos = TableQuery[Foos]

  //  def makeFoosTable(db: Database): Unit = {
  //    db.withSession { implicit session =>
  //      (foos.ddl).create
  //    }
  //  }
  //
  val facts = TableQuery[FactsInDB]

  //  def makeFactsTable(db: Database): Unit = {
  //    db.withSession { implicit session =>
  //      // Create the schema by combining the DDLs for the Suppliers and Coffees
  //      // tables using the query interfaces
  //      (facts.ddl).create
  //    }
  //  }
  //


}
