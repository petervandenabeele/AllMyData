/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util._

object InFactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData InFactsReader.main")
    val (dataFile, unusedSchemaFile, unusedContextFile) = getFileName(args)

    val dataFullFilename    = getAndLogFullFileName(dataFile,        "data",     "Reading from: ")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Left(csv.InFactsReader.reader)
    )

    handleResults(facts)
  }

}
