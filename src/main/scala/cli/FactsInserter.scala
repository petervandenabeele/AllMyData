/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
  }

  def reader(filename: String): List[Fact] = {
    println("reading from file " + filename)
    List()
  }
}
