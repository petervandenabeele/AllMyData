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
      val localContextString = elements(0)
      val localSubjectString = elements(2)
      val predicate = elements(4)
      val csvObjectType = elements(5)
      val csvObjectValue = elements(6)

      val localContextId: Option[Int] = localContextString match {
        case "" => None
        case _ => Some(localContextString.toInt)
      }

      val contextOption = localContextId match {
        case None => None
        case Some(i) => subjects.get(i)
      }

      val localSubjectId: Option[Int] = localSubjectString match {
        case "" => None
        case _ => Some(localSubjectString.toInt)
      }

      val subjectOption = localSubjectId match {
        case None => None
        case Some(i) => subjects.get(i)
      }

      val (objectType, objectValueOption, errorOption) = objectTypeValueTriple(
        csvObjectType, csvObjectValue, subjects)

      val factOption =
        if (objectValueOption == None)
          None // error occurred in finding link to objectValue in this file
        else
          Some(factFrom_CSV_Line(
            predicate = predicate,
            objectType = objectType,
            objectValue = objectValueOption.get,
            contextOption = contextOption,
            subjectOption = subjectOption))

      if (subjectOption.isEmpty && localSubjectId.nonEmpty && factOption.nonEmpty) {
        subjects += (localSubjectId.get -> factOption.get.subject)
      }

      (factOption, errorOption)
    }).filter(factWithStatus => factWithStatus._1.nonEmpty || factWithStatus._2.nonEmpty)
  }

  private def objectTypeValueTriple(
    csvObjectType: String,
    csvObjectValue: String,
    subjects: scala.collection.mutable.Map[Int, ATD_Subject]):

    (String, Option[String], Option[String]) =

      csvObjectType match {
        case "" => ("", None, None) // empty line
        case "c" => { // objectValue is link to earlier entry in this file
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

  private def factFrom_CSV_Line(predicate: ATD_Predicate,
                        objectType: ATD_ObjectType,
                        objectValue: ATD_ObjectValue,
                        contextOption: Option[ATD_Context],
                        subjectOption: Option[ATD_Subject]) = {
    val context = contextOption.getOrElse("")

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
