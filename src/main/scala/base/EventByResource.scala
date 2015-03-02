/**
 * Created by peter_v on 27/02/15.
 */

package base

case class EventByResource (resource: Option[Resource],
                            event: Option[Event],
                            error: Option[String] = None)
