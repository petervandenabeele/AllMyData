/**
 * Created by peter_v on 04/12/14.
 */

package cli

import csv.CSV_InFactReader.reader

/** WIP: Read s from a infacts CSV file and write Facts to a facts CSV file **/
object InFactsInserter {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData InFactsInserter.main")
    val filename = Util.getFileName(args)
    val fullFilename = Util.getFullFilename(filename)

    print("Reading from: ")
    println(fullFilename)

    insertFactsFromFile(fullFilename = fullFilename)
  }

  /** WIP show in println **/
  private def insertFactsFromFile(fullFilename: String): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator = reader(file)
    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        println(factOption.get)
      if (errorOption.nonEmpty)
        println(s"ERROR: In $fullFilename : ${errorOption.get}")
    })
  }
}
