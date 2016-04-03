/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util.readFactsFromFile
import base.Fact

object InFactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData InFactsReader.main")
    val filename = Util.getFileName(args)._1
    val fullFilename = Util.getFullFilename(filename, "data")
    print("Reading from: ")
    println(fullFilename)

    readFactsFromFile(
      fullFilename = fullFilename,
      readerEither = Left(csv.InFactsReader.reader)
    ).foreach(
      (fact: Fact) => println(fact)
    )
  }

}
