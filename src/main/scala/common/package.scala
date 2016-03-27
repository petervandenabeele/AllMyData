/**
  * Created by peter_v on 21/11/14.
  */

import java.util.UUID

import base.{Fact, EventByResource}

package object common {
  type ATD_TimeStamp = String
  type ATD_Uuid = String
  type ATD_Subject = UUID
  type ATD_Predicate = String
  // see list of object types below
  type ATD_ObjectType = String
  type ATD_ObjectValue = String

  // optional Fact and optional Error message
  type FactWithStatus = (Option[Fact], Option[String])
  type FactIterator = Iterator[FactWithStatus]
  type EventByResourceIterator = Iterator[EventByResource]

  def newUUID = UUID.randomUUID

  val separator = ";"
}

// ATD_ObjectType describes the type ObjectValue is one of:
// s : string (most generic)
// c : csv_reference (to a number in the CSV input, translated to a reference (r))
// t : time (date and/or time ISO, with up to 9 decimals for nanoseconds)
// u : URI (and external URI, may include prefixes)
// r : reference (to the subject of a resource, e.g. as a UUID)
// i : integer (can be signed)
// d : decimal (arbitrary precision, correct "decimal" behaviour)
// f : float (arbitrary precision, so could be "double" in practice)
