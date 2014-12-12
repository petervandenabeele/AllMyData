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
    val topic = args match {
      case Array() => "test_001"
      case Array(t)  => t
    }
    insertFromFile(filename = filename, topic = topic)
  }

  def insertFromFile(filename: String, topic: String): Unit = {
    val file = scala.io.Source.fromFile(filename)
    val factIterator = reader(file)
    val kafkaProducer = KafkaProducer(topic = topic)
    val factEncoder = new FactEncoder()
    factIterator.foreach(fact => {
      kafkaProducer.send(factEncoder.toBytes(fact), null)
    })
  }

  // reading from a CSV with structure (7 fields, last field no newlines)
  // local_context | context_uuid |
  // local_subject | subject_uuid |
  // predicate | objestType | object Value
  def reader(file: BufferedSource): Iterator[Fact] = {
    var subjects = scala.collection.mutable.Map[Int, ATD_Subject]()

    file.getLines().map[Fact](line => {
      val elements:Array[String] = line.split(",", 7)
      val local_subject_string = elements(2)
      val predicate = elements(4)
      val objectType = elements(5)
      val objectValue = elements(6)

      val local_id: Option[Int] = local_subject_string match {
        case "" => None
        case _ => Some(local_subject_string.toInt)
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
      if (subjectOption.isEmpty && local_id.nonEmpty) {
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
