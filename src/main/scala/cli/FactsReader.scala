/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util._

object FactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsReader.main")
    val (dataFile, unusedSchemaFile, unusedcontextFile) = Util.getFileName(args)

    val dataFullFilename    = getAndLogFullFileName(dataFile,     "Reading from: ")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Left(csv.FactsReader.reader)
    )

    handleResults(facts)
  }

}
