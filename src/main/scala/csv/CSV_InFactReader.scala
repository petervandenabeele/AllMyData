/**
 * Created by peter_v on 11/01/15.
 */

package csv

import base.{Context, Fact}
import common._

import scala.io.BufferedSource

/** Reads InFacts from a CSV
  *
  * Format is (statically defined):
  * no header line
  * local_context | context_uuid | local_subject | subject_uuid | predicate | objectType | objectValue
  *
  * There is a local correlation between local_context entries and reused in later facts
  * and local_subject reused in later local_subject and object fields
  * (see tests for examples e.g. two_facts_with_csv_reference.csv)
  */
object CSV_InFactReader {

  def reader(file: BufferedSource): FactIterator = {
    var subjects = scala.collection.mutable.Map[Int, ATD_Subject]()

    file.getLines().filterNot(x => x.isEmpty).map[FactWithStatus] (line => {
      val elements:Array[String] = line.split(separator, 7)
      val localContextString = elements(0)
      val localSubjectString = elements(2)
      val predicate = elements(4)
      val csvObjectType = elements(5)
      val csvObjectValue = elements(6)

      val (_, context) = getSubjectFromCache(localContextString, subjects)
      val (subjectIdOption, subjectOption) = getSubjectFromCache(localSubjectString, subjects)

      val (objectType, objectValueOption, errorOption) = objectTypeValueTriple(
        csvObjectType, csvObjectValue, subjects)


      if (objectValueOption.isEmpty)
        // error occurred in finding link to objectValue in this file
        (None, errorOption)
      else {
        val fact = factFrom_CSV_Line(
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValueOption.get.toString,
          context = Context(context),
          subjectOption = subjectOption)

        if (subjectOption.isEmpty && subjectIdOption.nonEmpty) {
          subjects += (subjectIdOption.get -> fact.subject)
        }
        (Some(fact), None)
      }
    }).filter(factWithStatus => factWithStatus._1.nonEmpty || factWithStatus._2.nonEmpty)
  }

  private type SubjectsMap = scala.collection.mutable.Map[Int, ATD_Subject]

  private def getSubjectFromCache(csvReference: String, subjects: SubjectsMap)
  :(Option[Int], Option[ATD_Subject]) = {
    val subjectIdOption: Option[Int] = csvReference match {
      case "" => None
      case s => Some(s.toInt)
    }
    val subjectOption = subjectIdOption match {
      case None => None
      case Some(i) => subjects.get(i)
    }
    (subjectIdOption, subjectOption)
  }

  private def objectTypeValueTriple(
    csvObjectType: String,
    csvObjectValue: String,
    subjects: SubjectsMap)
  :(ATD_ObjectType, Option[ATD_ObjectValue], Option[String]) = {
    csvObjectType match {
      case "" => ("", None, None) // empty line
      case "c" => // objectValue is link to earlier entry in this file
        val objectValueOption = subjects.get(csvObjectValue.toInt) match {
          case None => None
          case Some(x: ATD_Subject) => Some(x.toString)
        }
        val errorOption =
          if (objectValueOption.nonEmpty)
            None
          else
            Some(s"csvObjectValue $csvObjectValue could not be found")
        ("r", objectValueOption, errorOption)
      case _ => (csvObjectType, Some(csvObjectValue), None)
    }
  }

  private def factFrom_CSV_Line(predicate: ATD_Predicate,
                        objectType: ATD_ObjectType,
                        objectValue: ATD_ObjectValue,
                        context: Context,
                        subjectOption: Option[ATD_Subject]): Fact = {
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
