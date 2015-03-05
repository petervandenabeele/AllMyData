/**
 * Created by peter_v on 04/03/15.
 */

package slick

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ProvenShape}

class Foos(tag: Tag) extends Table[(Int, String)](tag, "FOOS") {
  // This is the primary key column:
  def id: Column[Int] = column[Int]("FOO_ID", O.PrimaryKey)
  def name: Column[String] = column[String]("FOO_NAME")

  def * : ProvenShape[(Int, String)] = (id, name)
}
