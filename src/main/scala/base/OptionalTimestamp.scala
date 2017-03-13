/**
  * Created by peter_v on 03/04/2016.
  */

package base

import com.github.nscala_time.time.Imports.DateTime

case class OptionalTimestamp(timestamp: Option[String]) {
  override def toString: String = timestamp.getOrElse("")
  def isDefined: Boolean = timestamp.isDefined
  def isEmpty: Boolean = timestamp.isEmpty
  def get: String = timestamp.get

  if (isDefined) {
    DateTime.parse(get)
  }
}

object OptionalTimestamp {
  def apply(timestamp: String): OptionalTimestamp = {
    timestamp match {
      case "" => new OptionalTimestamp(None)
      case _  => new OptionalTimestamp(Some(timestamp))
    }
  }
}  

