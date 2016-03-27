/**
 * Created by peter_v on 04/12/14.
 */

package cli

import csv.CSV_FactReader.reader

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
    val defaultFilename = "facts.csv"
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

    insertFactsFromFile(fullFilename = fullFilename, topic = topic)
  }

  private def insertFactsFromFile(fullFilename: String, topic: String): Unit = {
    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator = reader(file)
    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        println(factOption.get)
      if (errorOption.nonEmpty)
        println(s"ERROR: In $fullFilename : ${errorOption.get}")
    })
  }
}
