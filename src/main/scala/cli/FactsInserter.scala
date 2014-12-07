/**
 * Created by peter_v on 04/12/14.
 */

package cli

import base.Fact
import common._
import scala.io.BufferedSource

object FactsInserter {
  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsInserter.main")
  }

  // reading from a CSV with structure
  // optional local_id | optional subject | predicate | objestType | object Value
  def reader(file: BufferedSource): Iterator[Fact] = {
    var uuids = scala.collection.mutable.Map[Int, ATD_Uuid]()

    file.getLines().map[Fact](line => {
      val elements:Array[String] = line.split(",", 5)
      val local_id_string = elements(0)
      val predicate = elements(2)
      val objectType = elements(3)
      val objectValue = elements(4)

      val local_id: Option[Int] = local_id_string match {
        case "" => None
        case _ => Some(local_id_string.toInt)
      }
      val uuidOption = local_id match {
        case None => None
        case Some(i) => uuids.get(i)
      }
      val fact = factFrom_CSV_Line(
        predicate = predicate,
        objectType = objectType,
        objectValue = objectValue,
        uuidOption)
      if (uuidOption.isEmpty && !local_id.isEmpty) {
        uuids += (local_id.get -> fact.uuid)
      }
      fact
    })
  }

  def factFrom_CSV_Line(predicate: ATD_Predicate,
                        objectType: ATD_ObjectType,
                        objectValue: ATD_ObjectValue,
                        uuidOption: Option[ATD_Uuid]) = {
    uuidOption match {
      case Some(uuid) =>
        Fact(
          uuid = uuid,
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
      case None =>
        Fact(
          predicate = predicate,
          objectType = objectType,
          objectValue = objectValue
        )
    }
  }
}
