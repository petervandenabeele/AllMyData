/**
  * Created by peter_v on 13/03/14.
  */

package cli

import Util._

object EventsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData EventsReader.main")
    val (dataFile, unusedSchemaFile, contextFile) = Util.getFileName(args)
    if (contextFile.isEmpty) throw new RuntimeException("Also provide a contextFile as third argument")

    val dataFullFilename    = getAndLogFullFileName(dataFile,        "data",     "Reading from: ")
    val contextFullFilename = getAndLogFullFileName(contextFile.get, "data",     "With context: ")

    val (context, contextFacts) = contextAndFacts(contextFullFilename)
    println(s"context is $context")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Right(csv.EventsReader.reader),
      context = context
    )

    handleResults(contextFacts ++ facts)
  }

}
