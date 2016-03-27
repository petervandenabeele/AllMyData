/**
  * Created by peter_v on 22/02/15.
  */

package base

import common.{ATD_ObjectValue, ATD_ObjectType, ATD_Predicate}

case class PredicateObject(predicate: ATD_Predicate,
                           objectValue: ATD_ObjectValue,
                           objectType: ATD_ObjectType = "s") {
  PredicateObject.validatePredicateObject(
    predicate = predicate,
    objectValue = objectValue,
    objectType = objectType)
}

object PredicateObject {
  def validatePredicateObject(predicate: ATD_Predicate,
                              objectValue: ATD_ObjectValue,
                              objectType: ATD_ObjectType) {
    if (!Fact.validPredicates.contains(predicate))
      throw new IllegalArgumentException(s"The predicate $predicate is not in list of validPredicates")

    if (objectValue.isEmpty && objectType != "s")
      throw new IllegalArgumentException(s"The objectValue for a non-String cannot be empty for objectValue $predicate")
  }
}
