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

    val dataFullFilename = getFullFilename(dataFile, "data")
    print("Reading from: ")
    println(dataFullFilename)

    val schemaFullFilename = getFullFilename(schemaFile.get, "metadata")
    print("With schema: ")
    println(schemaFullFilename)

    val contextFullFilename = getFullFilename(contextFile.get, "data")
    print("With context: ")
    println(contextFullFilename)

    val (context, contextFacts) = contextAndFacts(contextFullFilename)
    println(s"context is $context")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Right(json.JsonEventsReader.reader),
      context = context,
      schemaFullFilename = Some(schemaFullFilename)
    )

    handleResults(contextFacts ++ facts)
  }

}
