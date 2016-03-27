/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

/** Reads events from a CSV file
  *
  * Format is:
  * First line: list of predicates (e.g. "atd:foo:name;atd:bar:size")
  * Second line: list of objectTypes (e.g. "s,i")
  * Next lines: per line, all values for the predicates for one new resource
  */

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    val potential_header = file.getLines()
    if (! potential_header.hasNext) return Iterator.empty

    val predicatesLine = potential_header.next()
    val predicates = predicatesLine.split(separator)
    val objectTypesLine = potential_header.next()
    val objectTypes = objectTypesLine.split(separator)

    file.getLines().map[EventByResource](line => {
      val objectValues = line.split(separator)
      val predicateObjects =
        predicates.
        zip(objectTypes).
        zip(objectValues).
        filter(p_ot_ov => ! p_ot_ov._2.isEmpty). // drop tuples with empty objectValues
        map { case ((predicate, objectType), objectValue) =>
          PredicateObject(predicate = predicate,
                          objectType = objectType,
                          objectValue = objectValue)
        }
      EventByResource(resource = Some(Resource()),
                      event = Some(Event(predicateObjects)))
    })
  }
}
