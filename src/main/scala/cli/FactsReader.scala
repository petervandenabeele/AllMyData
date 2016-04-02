/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util.readFactsFromFile

object FactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData FactsReader.main")
    val filename = Util.getFileName(args)._1
    val fullFilename = Util.getFullFilename(filename)
    print("Reading from: ")
    println(fullFilename)

    readFactsFromFile(
      fullFilename = fullFilename,
      readerEither = Left(csv.FactsReader.reader)
    )
  }

}
