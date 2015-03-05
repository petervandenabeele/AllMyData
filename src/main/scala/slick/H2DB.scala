/**
 * Created by peter_v on 04/03/15.
 */

package slick

import base.Fact

import scala.slick.driver.H2Driver.simple._

object H2DB {

  def foo: List[(Int, String)] = {
    val foos: TableQuery[Foos] = TableQuery[Foos]

    val db = Database.forURL("jdbc:h2:mem:hello", driver = "org.h2.Driver")

    db.withSession { implicit session =>
      // Create the schema by combining the DDLs for the Suppliers and Coffees
      // tables using the query interfaces
      (foos.ddl).create

      foos += (101, "foo is bar")
      foos += (102, "ping is tux")

      foos.list
    }
  }

  def insert_fact(fact: Fact): Unit = {
  }
}
