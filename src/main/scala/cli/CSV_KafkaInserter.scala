/**
 * Created by peter_v on 08/23/15.
 */

package cli

import csv.CSV_KafkaReader.reader
import encoding.FactEncoder
import kafkaStreaming.KafkaProducer

object CSV_KafkaInserter {
  // TODO : DRY this with other inserters
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData CSV_KafkaInserter.main")
    val defaultFilename = "kafkaFacts.csv"
    val defaultTopic = "out_001"
    val (filename, topic) = args match {
      case Array() => (defaultFilename, defaultTopic)
      case Array(f)  => (f, defaultTopic)
      case Array(f,t)  => (f,t)
    }
    val homeDir = System.getProperty("user.home")
    val fullFilename = homeDir + "/data/private/data/mnt/facts/" + filename

    print("Reading from: ")
    println(fullFilename)
    println(s"topic is $topic")

    insertFactsFromCSV_KafkaFile(fullFilename = fullFilename, topic = topic)
  }

  private def insertFactsFromCSV_KafkaFile(fullFilename: String, topic: String): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator = reader(file)
    val kafkaProducer = KafkaProducer(topic = topic)
    val factEncoder = new FactEncoder()
    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        kafkaProducer.send(factEncoder.toBytes(factOption.get), null)
      if (errorOption.nonEmpty)
        println(s"ERROR: In $fullFilename : ${errorOption.get}")
    })
  }
}
