/**
  * Created by peter_v on 22/02/15.
  */

package base

import common.{ATD_ObjectValue, ATD_ObjectType, ATD_Predicate}

case class PredicateObject(predicate: ATD_Predicate = "",
                           objectType: ATD_ObjectType = "s",
                           objectValue: ATD_ObjectValue = "")
