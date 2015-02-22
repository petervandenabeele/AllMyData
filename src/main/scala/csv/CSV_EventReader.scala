/**
 * Created by peter_v on 22/02/15.
 */

package csv

import base.{Event, Resource}
import common._

import scala.io.BufferedSource

object CSV_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    file.getLines().map[EventByResource] (line => {
      (Some(Resource(), Event()), None)
    })
  }
}
