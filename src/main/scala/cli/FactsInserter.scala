/**
 * Created by peter_v on 04/12/14.
 */

package cli

import encoding.FactEncoder
import csv.CSV_FactReader.reader

import kafkaStreaming.KafkaProducer

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
    val homeDir = System.getProperty("user.home")
    val filename = homeDir + "/data/private/data/input/input.csv"
    val topic = args match {
      case Array() => "test_001"
      case Array(t)  => t
    }
    insertFromFile(filename = filename, topic = topic)
  }

  private def insertFromFile(filename: String, topic: String): Unit = {
    val file = scala.io.Source.fromFile(filename)
    val factIterator = reader(file)
    val kafkaProducer = KafkaProducer(topic = topic)
    val factEncoder = new FactEncoder()
    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        kafkaProducer.send(factEncoder.toBytes(factOption.get), null)
      if (errorOption.nonEmpty)
        println(s"ERROR: In ${filename} : ${errorOption.get}")
    })
  }
}
