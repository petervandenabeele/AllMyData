/**
  * Created by peter_v on 21/11/14.
 */

import base.Fact

package object common {
  type ATD_TimeStamp = String
  type ATD_Uuid = String
  type ATD_Context = String
  type ATD_Subject = String
  type ATD_Predicate = String
  type ATD_ObjectType = String
  type ATD_ObjectValue = String

  type FactStream = Stream[Fact]
}
