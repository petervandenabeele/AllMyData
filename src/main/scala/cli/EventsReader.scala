/**
  * Created by peter_v on 13/03/14.
  */

package cli

import java.time.{ZoneId, ZonedDateTime}

import base._
import csv.CSV_EventReader.eventByResourceReader

/** WIP: Read events from an infacts file and write them to a facts file **/
object EventsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData EventsReader.main")
    val filename = Util.getFileName(args)
    val fullFilename = Util.getFullFilename(filename)

    print("Reading from: ")
    println(fullFilename)
    println(s"context is $contextFacts")

    readEventsFromFile(fullFilename = fullFilename, contextFacts = contextFacts)
  }

  /** Static contextFacts (still needed ??) **/
  private val contextFacts: Seq[Fact] = {

    val predicateObjects = List(
      PredicateObject(predicate = "amd:context:source", objectValue = "@peter_v", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:processor", objectValue = "vim", objectType = "s"), // replace this
      PredicateObject(predicate = "amd:context:ingress_time", objectValue = ZonedDateTime.now(ZoneId.of("UTC")).toString, objectType = "t"),
      PredicateObject(predicate = "amd:context:visibility", objectValue = "professional", objectType = "s"), // public | private | professional
      PredicateObject(predicate = "amd:context:encryption", objectValue = "encrypted", objectType = "s") // public | private | professional
    )

    val ebr = EventByResource(resource = Some(Resource()),
      event = Some(Event(predicateObjects)))
    factsFromEventByResource(ebr, Context(""))
  }

  /** Read the actual facts and insert them in output file **/
  private def readEventsFromFile(fullFilename: String, contextFacts: Seq[Fact]): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val eventByResourceIterator = eventByResourceReader(file)
    val context: Context = Context(contextFacts.head.subject.toString)

    contextFacts.foreach(fact =>
      println(fact.toString)
    )
    eventByResourceIterator.foreach(eventByResource => {
      if (eventByResource.resource.nonEmpty) {
        factsFromEventByResource(eventByResource, context).foreach(fact =>
          println(fact.toString)
        )
      }
      if (eventByResource.error.nonEmpty)
        println(s"ERROR: In $fullFilename : ${eventByResource.error.get}")
    })
  }

  /** Produce Facts from an EventByResource (move to EventByResource ??) **/
  private def factsFromEventByResource(eventByResource: EventByResource, context: Context): Seq[Fact] = {
    val resource = eventByResource.resource.get
    eventByResource.event.get.pos.map(predicateObject =>
      Fact(
        context = context,
        subject = resource.subject,
        predicate = predicateObject.predicate,
        objectType = predicateObject.objectType,
        objectValue = predicateObject.objectValue
      )
    )
  }
}
