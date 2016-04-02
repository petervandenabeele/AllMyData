/**
  * Created by peter_v on 13/03/14.
  */

package cli

import cli.Util.readFactsFromFile

object JsonEventsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData JsonEventsReader.main")
    val (dataFile, schemaFile) = Util.getFileName(args)
    if (schemaFile.isEmpty) throw new RuntimeException("Also provide a schemaFile as second argument")

    val dataFullFilename = Util.getFullFilename(dataFile)
    print("Reading from: ")
    println(dataFullFilename)

    val schemaFullFilename = Util.getFullFilename(schemaFile.get)
    print("With schema: ")
    println(schemaFullFilename)

    val (context, contextFacts) = contextAndFacts
    println(s"context is $context")
    println(s"contextFacts are $contextFacts")

    contextFacts.foreach(fact =>
      println(fact.toString)
    )

    readFactsFromFile(
      fullFilename = dataFullFilename,
      readerEither = Right(json.JsonEventsReader.reader),
      contextOption = Some(context),
      schemaFullFilename = Some(schemaFullFilename)
    )
  }

  import java.time.{ZoneId, ZonedDateTime}

  import base.EventByResource.factsFromEventByResource
  import base._

  /** Static contextFacts for bootstrapping. */
  private val contextAndFacts: (Context, Seq[Fact]) = {

    val predicateObjects = List(
      PredicateObject(predicate = "amd:context:source", objectValue = "Meetup", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:processor", objectValue = "getter", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:ingress_time", objectValue = ZonedDateTime.now(ZoneId.of("UTC")).toString, objectType = "t"),
      PredicateObject(predicate = "amd:context:visibility", objectValue = "professional", objectType = "s"), // public | private | professional
      PredicateObject(predicate = "amd:context:encryption", objectValue = "encrypted", objectType = "s") // public | private | professional
    )

    val resource = Resource()
    val eventByResource = EventByResource(
      resource = resource,
      event = Event(predicateObjects))
    (Context(Some(resource.subject)), factsFromEventByResource(eventByResource, Context(None)))
  }

}
