/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact
import common._
import encoding.FactEncoder
import kafkaStreaming.KafkaProducer
import scala.io.BufferedSource

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
    val filename = "/Users/peter_v/data/private/data/input.csv"
    insertFromFile(filename = filename)
  }

  def insertFromFile(filename: String): Unit = {
    val file = scala.io.Source.fromFile(filename)
    val factIterator = reader(file)
    val kafkaProducer = KafkaProducer()
    val factEncoder = new FactEncoder()
    factIterator.foreach(fact => {
      kafkaProducer.send(factEncoder.toBytes(fact), null)
    })
  }

  // reading from a CSV with structure
  // optional local_id | optional subject | predicate | objestType | object Value
  def reader(file: BufferedSource): Iterator[Fact] = {
    var subjects = scala.collection.mutable.Map[Int, ATD_Subject]()

    file.getLines().map[Fact](line => {
      val elements:Array[String] = line.split(",", 5)
      val local_id_string = elements(0)
      val predicate = elements(2)
      val objectType = elements(3)
      val objectValue = elements(4)

      val local_id: Option[Int] = local_id_string match {
        case "" => None
        case _ => Some(local_id_string.toInt)
      }
      val subjectOption = local_id match {
        case None => None
        case Some(i) => subjects.get(i)
      }
      val fact = factFrom_CSV_Line(
        predicate = predicate,
        objectType = objectType,
        objectValue = objectValue,
        subjectOption = subjectOption)
      if (subjectOption.isEmpty && !local_id.isEmpty) {
        subjects += (local_id.get -> fact.subject)
      }
      fact
    })
  }

  def factFrom_CSV_Line(predicate: ATD_Predicate,
                        objectType: ATD_ObjectType,
                        objectValue: ATD_ObjectValue,
                        subjectOption: Option[ATD_Subject]) = {
    subjectOption match {
      case Some(subject) =>
        Fact(
          subject = subject,
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
      case None =>
        Fact(
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
    }
  }
}
