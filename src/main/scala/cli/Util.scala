/**
  * Created by peter_v on 27/03/16.
  */

package cli

import base.{Context, Fact}
import common.FactIterator

import scala.io.BufferedSource

object Util {

  def getFileName(args: Array[String]) = args match {
    case Array(f) => f
    case _ => throw new RuntimeException("provide a filename to read from")
  }

  def getFullFilename(filename: String, dir: String = "data") = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/facts/$dir/" + filename
  }

  def readFactsFromFile(
                         fullFilename: String,
                         readerEither : Either[
                           BufferedSource => FactIterator,
                           (BufferedSource, Option[Context]) => FactIterator],
                         contextOption : Option[Context] = None): Unit = {

    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator =
      if (readerEither.isLeft)
        readerEither.left.get(file)
      else
        readerEither.right.get(file, contextOption)

    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        println(factOption.get)
      if (errorOption.nonEmpty)
        println(s"ERROR: In $fullFilename : ${errorOption.get}")
    })
  }

}
