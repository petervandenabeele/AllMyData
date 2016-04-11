/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util._

object InFactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData InFactsReader.main")
    val filename = getFileName(args)._1
    val fullFilename = getFullFilename(filename, "data")
    print("Reading from: ")
    println(fullFilename)

    val facts = readFactsFromFile(
      fullFilename = fullFilename,
      readerEither = Left(csv.InFactsReader.reader)
    )

    handleResults(facts)
  }

}
