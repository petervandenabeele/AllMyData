/**
  * Created by peter_v on 22/02/15.
  */

package base

import common._

case class PredicateObject(predicate: AMD_Predicate,
                           objectValue: AMD_ObjectValue,
                           objectType: AMD_ObjectType = "s",
                           at: OptionalTimestamp = OptionalTimestamp(None),
                           from: OptionalTimestamp = OptionalTimestamp(None),
                           to: OptionalTimestamp = OptionalTimestamp(None)) {
  if (!Fact.validPredicates.contains(predicate))
    throw new IllegalArgumentException(s"The predicate $predicate is not in list of validPredicates")

  if (objectValue.isEmpty && objectType != "s")
    throw new IllegalArgumentException(s"The objectValue for a non-String cannot be empty for objectValue $predicate")

  if (from.isDefined && to.isDefined && from.get > to.get)
    throw new IllegalArgumentException(s"The `from` $from cannot be larger than `to` $to timestamp")

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
