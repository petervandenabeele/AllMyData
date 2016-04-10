/**
  * Created by peter_v on 11/01/15.
  */

package csv

import base._
import common._

import scala.io.BufferedSource

/** Reads InFacts from a CSV
  *
  * Format is (statically defined):
  * no header line
  *   local_context
  *   context_id
  *   local_subject
  *   subject_id
  *   at
  *   from
  *   to
  *   predicate
  *   objectType
  *   objectValue
  *
  * There is a local correlation between local_context entries and reused in later facts
  * and local_subject reused in later local_subject and object fields
  * (see tests for examples e.g. two_facts_with_csv_reference.csv)
  */
object InFactsReader {

  def reader(file: BufferedSource): FactWithStatusIterator = {
    var subjects = scala.collection.mutable.Map[Int, AMD_Subject]()

    // NOTE : this is _not_ the Fact CSV format, first 4 fields are different;
    //        we could write a PredicateObject.fromCSV to DRY this.
    file.getLines().filterNot(x => x.isEmpty).map[FactWithStatus](line => {
      val elements: Array[String] = line.split(separator, 10)
      val localContextString = elements(0)
      val localSubjectString = elements(2)
      val at = elements(4)
      val from = elements(5)
      val to = elements(6)
      val predicate = elements(7)
      val csvObjectType = elements(8)
      val csvObjectValue = elements(9)

      val (_, context) = getSubjectFromCache(localContextString, subjects)
      val (subjectIdOption, subjectOption) = getSubjectFromCache(localSubjectString, subjects)

      val (objectTypeOption, objectValueOption, errorOption) = objectTypeValueTriple(
        csvObjectType, csvObjectValue, subjects
      )


      // empty line
      if (objectTypeOption.isEmpty) {
        (None, None)
      }
      // error occurred in finding link to objectValue in this file
      else if (objectValueOption.isEmpty) {
        assert(errorOption.isDefined, "There should be an error message defined here")
        (None, errorOption)
      }
      else {
        val predicateObject = PredicateObject(
          predicate = predicate,
          objectType = objectTypeOption.get,
          objectValue = objectValueOption.get.toString,
          at   = OptionalTimestamp(at),
          from = OptionalTimestamp(from),
          to   = OptionalTimestamp(to)
        )
        val fact = factFrom_CSV_Line(
          context = Context(context),
          subjectOption = subjectOption,
          predicateObject = predicateObject
        )

        if (subjectOption.isEmpty && subjectIdOption.nonEmpty) {
          subjects += (subjectIdOption.get -> fact.subject)
        }
        (Some(fact), None)
      }
    }).filterNot { case (factOption, errorOption) => factOption.isEmpty && errorOption.isEmpty }
  }

  private type SubjectsMap = scala.collection.mutable.Map[Int, AMD_Subject]

  private def getSubjectFromCache(csvReference: String, subjects: SubjectsMap)
  : (Option[Int], Option[AMD_Subject]) = {
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
  : (Option[AMD_ObjectType], Option[AMD_ObjectValue], Option[String]) = {
    csvObjectType match {
      case "" => (None, None, None) // empty line
      case "c" => // objectValue is link to earlier entry in this file
        val objectValueOption = subjects.get(csvObjectValue.toInt) match {
          case None => None
          case Some(x: AMD_Subject) => Some(x.toString)
        }
        val errorOption =
          if (objectValueOption.nonEmpty)
            None
          else
            Some(s"csvObjectValue $csvObjectValue could not be found")
        (Some("r"), objectValueOption, errorOption)
      case _ => (Some(csvObjectType), Some(csvObjectValue), None)
    }
  }

  private def factFrom_CSV_Line(context: Context,
                                subjectOption: Option[AMD_Subject],
                                predicateObject: PredicateObject): Fact = {
    subjectOption match {
      case Some(subject) =>
        Fact(
          context = context,
          subject = subject,
          predicateObject = predicateObject
        )
      case None =>
        Fact(
          context = context,
          predicateObject = predicateObject
        )
    }
  }
}
