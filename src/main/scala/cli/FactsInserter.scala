/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact
import scala.io.BufferedSource

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
  }

  // reading from a CSV with structure
  // optional local id | optional subject | predicate | objestType | object Value
  def reader(file: BufferedSource): Iterator[Fact] = {
    file.getLines().map[Fact](line => factFrom_CSV_Line(line))
  }

  def factFrom_CSV_Line(line: String) = {
    val elements:Array[String] = line.split(",", 5)
    Fact(
      predicate = elements(2),
      objectType = elements(3),
      objectValue = elements(4)
    )
  }
}
