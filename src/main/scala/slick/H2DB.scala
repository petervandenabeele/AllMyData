/**
 * Created by peter_v on 04/03/15.
 */

package slick

import scala.slick.driver.H2Driver.simple._

object H2DB {

  def q: Unit = {
    val facts: TableQuery[Facts] = TableQuery[Facts]

    val db = Database.forURL("jdbc:h2:mem:hello", driver = "org.h2.Driver")

    db.withSession { implicit session =>
      // Create the schema by combining the DDLs for the Suppliers and Coffees
      // tables using the query interfaces
      (facts.ddl).create

      facts += (101, "foo is bar")
      facts += (102, "ping is tux")

      val allFacts: List[(Int, String)] = facts.list

      println (allFacts)
    }
  }
}
