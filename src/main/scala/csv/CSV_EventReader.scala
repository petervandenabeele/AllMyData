/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    val potential_header = file.getLines().take(1)
    if (! potential_header.hasNext) return Iterator.empty
    val header = potential_header.next()
    val headers = header.split(",")

    file.getLines().map[EventByResource](line => {
      val resource = Some(Resource())

      val objectValues = line.split(",")
      val predicateObjects = headers.zip(objectValues).
        map { case (predicate, objectValue) =>
          PredicateObject(predicate = predicate, objectValue = objectValue )
        }
      val event = Some(Event(predicateObjects))

      EventByResource(resource = resource,
                      event = event)
    })
  }
}
