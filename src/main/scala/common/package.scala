/**
  * Created by peter_v on 21/11/14.
  */

import java.util.UUID

import base.{Fact, EventByResource}

package object common {
  type AMD_Timestamp = String
  type AMD_Id = UUID
  type AMD_Subject = UUID
  type AMD_Predicate = String
  // see list of object types in PredicateObject
  type AMD_ObjectType = String
  type AMD_ObjectValue = String

  // optional Fact and optional Error message
  type FactWithStatus = (Option[Fact], Option[String])
  type FactWithStatusIterator = Iterator[FactWithStatus]
  type EventByResourceIterator = Iterator[EventByResource]

  def newUUID = UUID.randomUUID

  val separator = ";"
}
