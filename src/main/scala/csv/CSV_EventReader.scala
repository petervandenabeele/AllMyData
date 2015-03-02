/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    val potential_headers = file.getLines().take(1)
    if (! potential_headers.hasNext) return Iterator.empty
    val headers = potential_headers.next
    file.getLines().map[EventByResource](line => {

      val resource = Some(Resource())
      val event = Some(Event(List(PredicateObject())))

      EventByResource(resource = resource,
                      event = event)
    })
  }
}
