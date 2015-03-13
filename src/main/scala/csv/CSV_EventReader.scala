/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    val potential_header = file.getLines()
    if (! potential_header.hasNext) return Iterator.empty
    val header = potential_header.next()
    val headers = header.split(",")

    val objectTypesLine = potential_header.next()
    val objectTypes = objectTypesLine.split(",")

    file.getLines().map[EventByResource](line => {
      val resourceOption = Some(Resource())

      val objectValues = line.split(",")
      val predicateObjects = headers.zip(objectTypes).zip(objectValues).
        map { case ((predicate, objectType), objectValue) =>
          PredicateObject(predicate = predicate,
                          objectType = objectType,
                          objectValue = objectValue )
        }
      val eventOption = Some(Event(predicateObjects))

      EventByResource(resource = resourceOption,
                      event = eventOption)
    })
  }
}
