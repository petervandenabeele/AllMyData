/**
  * Created by peter_v on 22/02/15.
  */

package base

/** Event is a seq of predicateObjects about a resource **/
case class Event(pos: Seq[PredicateObject] = Nil)
