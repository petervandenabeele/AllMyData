/**
  * Created by peter_v on 22/02/15.
  */

package base

import common.{AMD_ObjectValue, AMD_ObjectType, AMD_Predicate}

case class PredicateObject(predicate: AMD_Predicate,
                           objectValue: AMD_ObjectValue,
                           objectType: AMD_ObjectType = "s") {
  PredicateObject.validatePredicateObject(
    predicate = predicate,
    objectValue = objectValue,
    objectType = objectType)
}

object PredicateObject {
  def validatePredicateObject(predicate: AMD_Predicate,
                              objectValue: AMD_ObjectValue,
                              objectType: AMD_ObjectType) {
    if (!Fact.validPredicates.contains(predicate))
      throw new IllegalArgumentException(s"The predicate $predicate is not in list of validPredicates")

    if (objectValue.isEmpty && objectType != "s")
      throw new IllegalArgumentException(s"The objectValue for a non-String cannot be empty for objectValue $predicate")
  }
}
