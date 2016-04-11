/**
  * Created by peter_v on 13/03/14.
  */

package cli

import Util._

object EventsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData EventsReader.main")
    val (dataFile, unusedSchemaFile, contextFile) = Util.getFileName(args)

    val dataFullFilename = Util.getFullFilename(dataFile, "data")
    print("Reading from: ")
    println(dataFullFilename)

    val (context, contextFacts) = contextAndFacts
    println(s"context is $context")

    val facts = readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Right(csv.EventsReader.reader),
      context = context
    )

    handleResults(contextFacts ++ facts)
  }

  import java.time.{ZoneId, ZonedDateTime}
  import base._
  import base.EventByResource.factsFromEventByResource

  /** Static contextFacts for bootstrapping. */
  private val contextAndFacts: (Context, Iterator[Fact]) = {

    val predicateObjects = List(
      PredicateObject(predicate = "amd:context:source", objectValue = "@peter_v", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:processor", objectValue = "vim", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:ingress_time", objectValue = Fact.now, objectType = "t"),
      PredicateObject(predicate = "amd:context:visibility", objectValue = "professional", objectType = "s"), // public | private | professional
      PredicateObject(predicate = "amd:context:encryption", objectValue = "encrypted", objectType = "s") // public | private | professional
    )

    val resource = Resource()
    val eventByResource = EventByResource(
      resource = resource,
      event = Event(predicateObjects))
    (Context(Some(resource.subject)), factsFromEventByResource(eventByResource, Context(None)).toIterator)
  }

}
