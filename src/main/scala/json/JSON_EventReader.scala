/**
 * Created by peter_v on 17/04/15.
 */

package json

import base.{PredicateObject, Event, Resource, EventByResource}
import common._

import scala.io.BufferedSource

object JSON_EventReader {

  def eventByResourceReader(file: BufferedSource): EventByResourceIterator = {
    return Iterator.empty
  }
}

