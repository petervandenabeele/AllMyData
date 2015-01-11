/**
 * Created by peter_v on 11/01/15.
 */

package csv_reader

import base.Fact
import common._

import scala.io.BufferedSource

object CSV_Reader {

  // reading from a CSV with structure (7 fields, last field no newlines)
  // local_context | context_uuid |
  // local_subject | subject_uuid |
  // predicate | objectType | objectValue
  def reader(file: BufferedSource): FactIterator = {
    var subjects = scala.collection.mutable.Map[Int, ATD_Subject]()

    file.getLines().map[FactWithStatus] (line => {
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

      val objectTypeValueTriple : (String, Option[String], Option[String]) =
        csvObjectType match {
          case "" => ("", None, None)
          case "c" => {
            val objectValueOption = subjects.get(csvObjectValue.toInt)
            val errorOption =
              if (objectValueOption.nonEmpty)
                None
              else
                Some(s"csvObjectValue $csvObjectValue could not be found")
            ("r", objectValueOption, errorOption)
          }
          case _ => (csvObjectType, Some(csvObjectValue), None)
        }

      val factOption =
        if (objectTypeValueTriple._2 == None)
          None
        else
          Some(factFrom_CSV_Line(
            predicate = predicate,
            objectType = objectTypeValueTriple._1,
            objectValue = objectTypeValueTriple._2.get,
            contextOption = contextOption,
            subjectOption = subjectOption))

      if (subjectOption.isEmpty && local_subject_id.nonEmpty && factOption.nonEmpty) {
        subjects += (local_subject_id.get -> factOption.get.subject)
      }

      val errorOption = objectTypeValueTriple._3

      (factOption, errorOption)
    }).filter(factWithStatus => factWithStatus._1.nonEmpty || factWithStatus._2.nonEmpty)
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
