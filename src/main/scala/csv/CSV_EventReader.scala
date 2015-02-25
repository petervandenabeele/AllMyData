/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{Event, Resource}
import common._

import scala.io.BufferedSource

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    val potential_headers = file.getLines().take(1)
    if (! potential_headers.hasNext) return Iterator.empty
    val headers = potential_headers.next
    file.getLines().drop(1).map[EventByResource](line => {
      (Some(Resource(), Event()), None)
    })
  }
}
