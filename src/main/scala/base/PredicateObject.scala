/**
  * Created by peter_v on 22/02/15.
  */

package base

import java.util.UUID
import com.github.nscala_time.time.Imports._

import common._

case class PredicateObject(predicate: AMD_Predicate,
                           objectValue: AMD_ObjectValue,
                           objectType: AMD_ObjectType = "s",
                           at: OptionalTimestamp = OptionalTimestamp(Fact.today),
                           from: OptionalTimestamp = OptionalTimestamp(None),
                           to: OptionalTimestamp = OptionalTimestamp(None)) {

  if (!Fact.validPredicates.contains(predicate))
    throw new IllegalArgumentException(s"The predicate $predicate is not in list of validPredicates")

  if (from.isDefined && to.isDefined && from.get > to.get)
  throw new IllegalArgumentException(s"The `from` $from cannot be larger than `to` $to timestamp")

  if (!PredicateObject.supportedObjectTypes.contains(objectType))
    throw new IllegalArgumentException(s"The objectType $objectType is not in supported ${PredicateObject.supportedObjectTypes}")

  if (objectType == "t") {
    if (!objectValue.matches("""^\d\d\d\d-\d\d-\d\d.*"""))
      throw new IllegalArgumentException(s"Time field must start with at least a 4d-2d-2d date; was $objectValue")
    DateTime.parse(objectValue)
  }

  if (objectType == "r") {
    try {
      UUID.fromString(objectValue)
    } catch {
      case e: java.lang.IllegalArgumentException =>
        throw new IllegalArgumentException(s"This reference objectValue `$objectValue` could not be interpreted as UUID")
    }
  }

  if (objectType == "i") {
    try {
      objectValue.toInt
    } catch {
      case e: java.lang.NumberFormatException =>
        throw new IllegalArgumentException(s"This integer objectValue `$objectValue` could not be transformed to Int")
    }
  }

  if (objectType == "d") {
    try {
      BigDecimal(objectValue)
    } catch {
      case e: java.lang.NumberFormatException =>
        throw new IllegalArgumentException(s"This decimal objectValue `$objectValue` could not be transformed to BigDecimal")
    }
  }

  override def toString: String = {
    List(
      predicate,
      objectType,
      objectValue,
      at,
      from,
      to
    ).mkString(separator)
  }
}

object PredicateObject {
  val supportedObjectTypes = List(
    "s", // string (most generic)
    "t", // time (date and optional time ISO, with up to 9 decimals for nanoseconds)
    "u", // URI (and external URI, may include prefixes), not yet validated
    "r", // reference (to the subject of a resource, e.g. as a UUID)
    "i", // integer (can be signed)
    "d"  // decimal (arbitrary precision, correct "decimal" behaviour)
  )
  // TODO : support "f" float
  def errorPredicateObject(errorMsg: String) = {
    PredicateObject(predicate = "amd:error", objectValue = errorMsg, objectType = "s")
  }
}
