/**
 * Created by peter_v on 27/02/15.
 */

package base

/** An [[Event]] for a specific [[Resource]]
  *
  * @param resource an optional [[Resource]]
  * @param event an optional [[Event]]
  * @param error an optional error message
  */
case class EventByResource (resource: Option[Resource],
                            event: Option[Event],
                            error: Option[String] = None)
