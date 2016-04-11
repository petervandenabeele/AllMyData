/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util._

object FactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsReader.main")
    val filename = getFileName(args)._1
    val fullFilename = getFullFilename(filename, "data")
    print("Reading from: ")
    println(fullFilename)

    val facts = readFactsFromFile(
      fullFilename = fullFilename,
      readerEither = Left(csv.FactsReader.reader)
    )

    handleResults(facts)
  }

}
