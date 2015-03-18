/**
 * Created by peter_v on 13/03/14.
 */

package cli

import base.{EventByResource, Fact, Event, Resource}
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

    insertEventsFromFile(fullFilename = fullFilename, topic = topic)
  }

  private def insertEventsFromFile(fullFilename: String, topic: String): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val eventByResourceIterator = eventByResourceReader(file)
    val kafkaProducer = KafkaProducer(topic = topic)
    val factEncoder = new FactEncoder()
    eventByResourceIterator.foreach(eventByResource => {
      if (eventByResource.resource.nonEmpty) {
        factsFromEventByResource(eventByResource).foreach(fact =>
          kafkaProducer.send(factEncoder.toBytes(fact), null)
        )
      }
      if (eventByResource.error.nonEmpty)
        println(s"ERROR: In ${fullFilename} : ${eventByResource.error.get}")
    })
  }

  private def factsFromEventByResource(eventByResource: EventByResource): Seq[Fact] = {
    val resource = eventByResource.resource.get
    eventByResource.event.get.pos.map ( predicateObject =>
      Fact(
        subject = resource.subject,
        predicate = predicateObject.predicate,
        objectType = predicateObject.objectType,
        objectValue = predicateObject.objectValue
      )
    )
  }
}
