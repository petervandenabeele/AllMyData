/**
  * Created by peter_v on 22/02/15.
  */

package csv

import base.EventByResource._
import base.{Context, _}
import common._

import scala.io.BufferedSource

/** Reads events from a CSV eventFile
  *
  * Format is:
  * First line: list of predicates (e.g. "amd:foo:name;amd:bar:size")
  * Second line: list of objectTypes (e.g. "s,i")
  * Next lines: one new resource per line, with all values for the predicates in the cells
  */

object EventsReader {

  /** Read the actual facts and print them */
  def reader(eventFile: BufferedSource,
             context: Context = Context(None),
             unusedSchemaFile: Option[BufferedSource] = None,
             factsAtOption: Option[String] = None): FactWithStatusIterator = {
    if (unusedSchemaFile.isDefined) throw new RuntimeException("unusedSchemaFile should not be defined")
    val eventByResourceIterator = eventByResourceReader(eventFile, factsAtOption)

    eventByResourceIterator.flatMap[FactWithStatus](eventByResource => {
      factsFromEventByResource(eventByResource, context).map(fact => (Some(fact), None))
    })
  }

  // TODO only success case covered here
  def eventByResourceReader(eventFile: BufferedSource,
                            factsAtOption: Option[String] = None): EventByResourceIterator = {
    val potential_header = eventFile.getLines()
    if (!potential_header.hasNext) return Iterator.empty

    val predicatesLine = potential_header.next()
    val predicates = predicatesLine.split(separator)
    val objectTypesLine = potential_header.next()
    val objectTypes = objectTypesLine.split(separator)

    eventFile.getLines().filterNot(x => x.isEmpty).map[EventByResource](line => {
      val objectValues = line.split(separator)
      val predicateObjects =
        predicates.
          zip(objectTypes).
          zip(objectValues).
          filter(p_ot_ov => !p_ot_ov._2.isEmpty). // drop tuples with empty objectValues
          map { case ((predicate, objectType), objectValue) =>
            PredicateObject(
              predicate = predicate,
              objectValue = objectValue,
              objectType = objectType,
              factsAtOption = factsAtOption)
            }
      EventByResource(
        resource = Resource(),
        event = Event(predicateObjects))
    })
  }
}
