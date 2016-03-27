/**
 * Created by peter_v on 13/03/14.
 */

package cli

import java.time.{ZoneId, ZonedDateTime}

import base._
import common._
import csv.CSV_EventReader.eventByResourceReader

object EventsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData EventsInserter.main")
    val filename = args match {
      case Array(f)  => f
      case _  => throw new RuntimeException("provide a filename to read from")
    }
    val homeDir = System.getProperty("user.home")
    val fullFilename = homeDir + "/pp/facts/data/" + filename

    print("Reading from: ")
    println(fullFilename)
    println(s"context is $contextFacts")

    insertEventsFromFile(fullFilename = fullFilename, contextFacts = contextFacts)
  }

  private val contextFacts: Seq[Fact] = {

    val predicateObjects = List(
      PredicateObject(predicate = "amd:context:source",
        objectType = "s",
        objectValue = "@peter_v"), // replace this
      PredicateObject(predicate = "amd:context:processor",
        objectType = "s",
        objectValue = "vim"), // replace this
      PredicateObject(predicate = "amd:context:ingress_time",
        objectType = "t",
        objectValue = ZonedDateTime.now(ZoneId.of("UTC")).toString),
      PredicateObject(predicate = "amd:context:visibility",
        objectType = "s",
        objectValue = "professional") // public | private | professional
    )

    val ebr = EventByResource(resource = Some(Resource()),
                              event = Some(Event(predicateObjects)))
    factsFromEventByResource(ebr, Context(""))
  }


  private def insertEventsFromFile(fullFilename: String, contextFacts: Seq[Fact]): Unit = {
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

  private def factsFromEventByResource(eventByResource: EventByResource, context: Context): Seq[Fact] = {
    val resource = eventByResource.resource.get
    eventByResource.event.get.pos.map ( predicateObject =>
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
