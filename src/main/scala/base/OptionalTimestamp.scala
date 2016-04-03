/**
  * Created by peter_v on 03/04/2016.
  */

package base

case class OptionalTimestamp(timestamp: Option[String]) {
  override def toString: String = timestamp.getOrElse("")
  def isDefined = timestamp.isDefined
  def isEmpty = timestamp.isEmpty
  def get = timestamp.get
}

object OptionalTimestamp {
  def apply(timestamp: String): OptionalTimestamp = {
    timestamp match {
      case "" => new OptionalTimestamp(None)
      case _  => new OptionalTimestamp(Some(timestamp))
    }
  }
}  

