/**
  * Created by peter_v on 13/03/14.
  */

package cli

import Util._

object JsonEventsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData JsonEventsReader.main")
    val (dataFile, schemaFile, contextFile) = getFileName(args)
    if (schemaFile.isEmpty) throw new RuntimeException("Also provide a schemaFile as second argument")
    if (contextFile.isEmpty) throw new RuntimeException("Also provide a contextFile as third argument")

    val dataFullFilename    = getAndLogFullFileName(dataFile,        "Reading from: ")
    val schemaFullFilename  = getAndLogFullFileName(schemaFile.get,  "With schema:  ")
    val contextFullFilename = getAndLogFullFileName(contextFile.get, "With context: ")

    val (context, contextFacts, factsAtOption) = contextAndFacts(contextFullFilename)
    println(s"context is $context")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Right(json.JsonEventsReader.reader),
      context = context,
      schemaFullFilename = Some(schemaFullFilename),
      factsAtOption = factsAtOption
    )

    handleResults(contextFacts ++ facts)
  }

}
