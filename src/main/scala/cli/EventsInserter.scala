/**
 * Created by peter_v on 13/03/14.
 */

package cli

import java.time.{ZoneId, ZonedDateTime}

import base._
import common._
import csv.CSV_EventReader.eventByResourceReader
import encoding.FactEncoder
import kafkaStreaming.KafkaProducer

object EventsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData EventsInserter.main")
    val defaultFilename = "events.csv"
    val defaultTopic = "test_001"
    val (filename, topic) = args match {
      case Array() => (defaultFilename, defaultTopic)
      case Array(f)  => (f, defaultTopic)
      case Array(f,t)  => (f,t)
    }
    val homeDir = System.getProperty("user.home")
    val fullFilename = homeDir + "/data/private/data/mnt/input/" + filename

    print("Reading from: ")
    println(fullFilename)
    println(s"topic is $topic")
    println(s"context is $contextFacts")

    insertEventsFromFile(fullFilename, topic, contextFacts)
  }

  private val contextFacts: Seq[Fact] = {

    val predicateObjects = List(
      PredicateObject(predicate = "atd:context:source",
        objectType = "s",
        objectValue = "Data source"), // replace this
      PredicateObject(predicate = "atd:context:processor",
        objectType = "s",
        objectValue = "@peter_v"), // replace this
      PredicateObject(predicate = "atd:context:ingress_time",
        objectType = "t",
        objectValue = ZonedDateTime.now(ZoneId.of("UTC")).toString),
      PredicateObject(predicate = "atd:context:visibility",
        objectType = "s",
        objectValue = "professional") // public | private | professional
    )

    val ebr = EventByResource(resource = Some(Resource()),
                              event = Some(Event(predicateObjects)))
    factsFromEventByResource(ebr, Context(""))
  }

  private def insertEventsFromFile(fullFilename: String, topic: String, contextFacts: Seq[Fact]): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val eventByResourceIterator = eventByResourceReader(file)
    val kafkaProducer = KafkaProducer(topic = topic)
    val factEncoder = new FactEncoder()
    val context: Context = Context(contextFacts.head.subject.toString)
    contextFacts.foreach(fact =>
      kafkaProducer.send(factEncoder.toBytes(fact), null)
    )
    eventByResourceIterator.foreach(eventByResource => {
      if (eventByResource.resource.nonEmpty) {
        factsFromEventByResource(eventByResource, context).foreach(fact =>
          kafkaProducer.send(factEncoder.toBytes(fact), null)
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
