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

// ATD_ObjectType describes the type ObjectValue is one of:
// s : string (most generic)
// c : csv_reference (to a number in the CSV input, translated to a uuid (u))
// t : time (date and/or time ISO, with up to 9 decimals for nanoseconds)
// u : URI (and external URI, may include prefixes)
// r : reference (to the subject of a resource, e.g. as a UUID)
// i : integer (can be signed)
// d : decimal
// f : float (arbitrary precision)
