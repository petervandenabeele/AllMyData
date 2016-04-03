/**
  * Created by peter_v on 27/02/15.
  */

package base

/** An [[Event]] for a specific [[Resource]]
  *
  * @param resource a [[Resource]]
  * @param event    an [[Event]]
  */
case class EventByResource(resource: Resource,
                           event: Event)

object EventByResource {

  def factsFromEventByResource(eventByResource: EventByResource, context: Context): Seq[Fact] = {
    val resource = eventByResource.resource
    eventByResource.event.pos.map(predicateObject =>
      Fact(
        context = context,
        subject = resource.subject,
        predicateObject = predicateObject)
    )
  }

}
