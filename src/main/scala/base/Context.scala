/**
 * Created by peter_v on 11/03/15.
 */

package base

import java.util.UUID

case class Context (context: Option[UUID]) {
  override def toString(): String =
    context match {
      case None => ""
      case Some(uuid: UUID) => uuid.toString
    }
}

object Context {
  def apply(s: String): Context = {
    s match {
      case "" => new Context(None)
      case _s: String => new Context(Some(UUID.fromString(_s)))
    }
  }
}
