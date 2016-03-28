/**
  * Created by peter_v on 27/02/15.
  */

package base

/** An [[Event]] for a specific [[Resource]]
  *
  * @param resource an optional [[Resource]]
  * @param event    an optional [[Event]]
  * @param error    an optional error message
  */
case class EventByResource(resource: Option[Resource],
                           event: Option[Event],
                           error: Option[String] = None)

object EventByResource {

  def factsFromEventByResource(eventByResource: EventByResource, context: Context): Seq[Fact] = {
    val resource = eventByResource.resource.get
    eventByResource.event.get.pos.map(predicateObject =>
      Fact(
        context = context,
        subject = resource.subject,
        predicate = predicateObject.predicate,
        objectType = predicateObject.objectType,
        objectValue = predicateObject.objectValue
      )
    )
  }

}
