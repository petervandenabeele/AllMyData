/**
  * Created by peter_v on 22/02/15.
  */

package base

import java.util.UUID
import com.github.nscala_time.time.Imports._

import common._

class PredicateObject(predicate_ : AMD_Predicate,
                      objectValue_ : AMD_ObjectValue,
                      objectType_ : AMD_ObjectType = PredicateObject.defaultObjectType,
                      at_ : OptionalTimestamp = PredicateObject.defaultAt,
                      from_ : OptionalTimestamp = PredicateObject.defaultFrom,
                      to_ : OptionalTimestamp = PredicateObject.defaultTo) {

  def predicate: AMD_Predicate = predicate_
  def objectValue: AMD_ObjectValue = objectValue_
  def objectType: AMD_ObjectType = objectType_
  def at: OptionalTimestamp = at_
  def from: OptionalTimestamp = from_
  def to: OptionalTimestamp = to_

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

  if (objectType == "b") {
    if (!List("true", "false").contains(objectValue))
      throw new IllegalArgumentException(s"amd:boolean must be 'true' or 'false'")
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
      at,
      from,
      to,
      predicate,
      objectType,
      objectValue
    ).mkString(separator)
  }

  override def equals(that: Any): Boolean =
    that match {
      case that: PredicateObject => this.toString == that.toString
      case _ => false
    }

  override def hashCode: Int = this.toString.hashCode
}

object PredicateObject {
  val defaultObjectType = "s"
  val defaultAt = OptionalTimestamp(Fact.today)
  val defaultFrom = OptionalTimestamp(None)
  val defaultTo = OptionalTimestamp(None)

  // TODO : support "f" float (issue: JSON fields now safely parsed as decimal)
  val supportedObjectTypes = List(
    "s", // string (most generic)
    "t", // time (date and optional time ISO, with up to 9 decimals for nanoseconds)
    "u", // URI (and external URI, may include prefixes), not yet validated
    "r", // reference (to the subject of a resource, e.g. as a UUID)
    "i", // integer (can be signed)
    "d", // decimal (arbitrary precision, correct "decimal" behaviour)
    "b"  // boolean (from a JSON with true, false, "true", "false" value)
  )

  def errorPredicateObject(errorMsg: String): PredicateObject = {
    PredicateObject(predicate = "amd:error", objectValue = errorMsg)
  }

  def apply (predicate: AMD_Predicate,
             objectValue: AMD_ObjectValue,
             objectType: AMD_ObjectType = PredicateObject.defaultObjectType,
             factsAtOption: Option[String] = None,
             from: OptionalTimestamp = PredicateObject.defaultFrom,
             to: OptionalTimestamp = PredicateObject.defaultTo): PredicateObject = {
    new PredicateObject(
      predicate_ = predicate,
      objectValue_ = objectValue,
      objectType_ = objectType,
      at_ = if (factsAtOption.isDefined) OptionalTimestamp(factsAtOption.get) else defaultAt,
      from_ = from,
      to_ = to
    )
  }

}
