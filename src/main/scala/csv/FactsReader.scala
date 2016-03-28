/**
  * Created by peter_v on 08/23/15.
  */

package csv

import java.util.UUID

import base.{Context, Fact}
import common._

import scala.io.BufferedSource

object FactsReader {

  // reading from a CSV with structure (basic Fact, 7 fields, last field no newlines)
  // timeStamp
  // uuid
  // context
  // subject
  // predicate
  // objectType
  // objectValue
  def reader(file: BufferedSource): FactIterator = {
    file.getLines().map[FactWithStatus](line => {
      val elements: Array[String] = line.split(separator, 7)
      val timeStamp = elements(0)
      val id = UUID.fromString(elements(1)) // fail fast for invalid UUID string
      val context = elements(2)
      val subject = elements(3)
      val predicate = elements(4)
      val objectType = elements(5)
      val objectValue = elements(6)

      val fact = Fact(
        timeStamp = timeStamp,
        id = id,
        context = Context(context),
        subject = UUID.fromString(subject),
        predicate = predicate,
        objectType = objectType,
        objectValue = objectValue
      )
      (Some(fact), None) // errors not yet handled
    })
  }
}
