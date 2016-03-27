/**
  * Created by peter_v on 04/12/14.
  */

package cli

import Util.readFactsFromFile

object InFactsReader {

  def main(args: Array[String]): Unit = {
    println("Starting AllMyData InFactsReader.main")
    val filename = Util.getFileName(args)
    val fullFilename = Util.getFullFilename(filename)
    print("Reading from: ")
    println(fullFilename)

    readFactsFromFile(fullFilename = fullFilename, csv.CSV_InFactReader.reader)
  }

}
