/**
  * Created by peter_v on 08/23/15.
  */

package csv

import java.util.UUID

import base._
import common._

import scala.io.BufferedSource

object FactsReader {

  // reading from a CSV with structure (basic Fact, 10 fields, last field no newlines)
  // timestamp
  // uuid
  // context
  // subject
  // predicate
  // objectType
  // objectValue
  // at
  // from
  // to
  def reader(file: BufferedSource): FactIterator = {
    file.getLines().map[FactWithStatus](line => {
      val elements: Array[String] = line.split(separator, 10)
      val timestampString = elements(0)
      val id = UUID.fromString(elements(1)) // fail fast for invalid UUID string
      val context = elements(2)
      val subject = elements(3)
      val predicate = elements(4)
      val objectType = elements(5)
      val objectValue = elements(6)
      val at = elements(7)
      val from = elements(8)
      val to = elements(9)

      val predicateObject = PredicateObject(
        predicate = predicate,
        objectValue = objectValue,
        objectType = objectType,
        at   = OptionalTimestamp(at),
        from = OptionalTimestamp(from),
        to   = OptionalTimestamp(to)
      )

      val fact = Fact(
        timestamp = timestampString,
        id = id,
        context = Context(context),
        subject = UUID.fromString(subject),
        predicateObject
      )
      (Some(fact), None) // errors not yet handled
    })
  }
}
