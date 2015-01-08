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
    factIterator.foreach(factWithStatus => {
      val fact = factWithStatus._1
      kafkaProducer.send (factEncoder.toBytes (fact), null)
    })
  }

  // reading from a CSV with structure (7 fields, last field no newlines)
  // local_context | context_uuid |
  // local_subject | subject_uuid |
  // predicate | objectType | objectValue
  def reader(file: BufferedSource): FactIterator = {
    var subjects = scala.collection.mutable.Map[Int, ATD_Subject]()

    file.getLines().map[(Fact, Option[String])] (line => {
      val elements:Array[String] = line.split(",", 7)
      val local_context_string = elements(0)
      val local_subject_string = elements(2)
      val predicate = elements(4)
      val csvObjectType = elements(5)
      val csvObjectValue = elements(6)

      val local_context_id: Option[Int] = local_context_string match {
        case "" => None
        case _ => Some(local_context_string.toInt)
      }

      val contextOption = local_context_id match {
        case None => None
        case Some(i) => subjects.get(i)
      }

      val local_subject_id: Option[Int] = local_subject_string match {
        case "" => None
        case _ => Some(local_subject_string.toInt)
      }

      val subjectOption = local_subject_id match {
        case None => None
        case Some(i) => subjects.get(i)
      }

      val objectTypeValuePair =
        if (csvObjectType == "c") {
          val ObjectValueOption = subjects.get(csvObjectValue.toInt)
          val ObjectValue =
            if(ObjectValueOption.isEmpty)
              throw new RuntimeException(s"The csvObjectValue $csvObjectValue was not previously defined")
            else
              ObjectValueOption.get
          ("r", ObjectValue)
        }
        else
          (csvObjectType, csvObjectValue)

      val fact = factFrom_CSV_Line(
        predicate = predicate,
        objectType = objectTypeValuePair._1,
        objectValue = objectTypeValuePair._2,
        contextOption = contextOption,
        subjectOption = subjectOption)

      if (subjectOption.isEmpty && local_subject_id.nonEmpty) {
        subjects += (local_subject_id.get -> fact.subject)
      }
      (fact, None)
    })
  }

  def factFrom_CSV_Line(predicate: ATD_Predicate,
                        objectType: ATD_ObjectType,
                        objectValue: ATD_ObjectValue,
                        contextOption: Option[ATD_Context],
                        subjectOption: Option[ATD_Subject]) = {
    val context = contextOption match {
      case Some(c) => c
      case None => ""
    }

    subjectOption match {
      case Some(subject) =>
        Fact(
          context = context,
          subject = subject,
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
      case None =>
        Fact(
          context = context,
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
    }
  }
}
