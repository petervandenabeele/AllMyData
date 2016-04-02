/**
  * Created by peter_v on 27/03/16.
  */

package cli

import base.{Context, Fact}
import common.FactIterator

import scala.io.BufferedSource

object Util {

  def getFileName(args: Array[String]): (String, Option[String]) = args match {
    case Array(dataFile) => (dataFile, None)
    case Array(dataFile, schemaFile) => (dataFile, Some(schemaFile))
    case _ => throw new RuntimeException("provide a dataFile (and optional schemaFile) to read from")
  }

  def getFullFilename(filename: String, dir: String = "data") = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/facts/$dir/" + filename
  }

  def readFactsFromFile(
                         fullFilename: String,
                         readerEither : Either[
                           BufferedSource => FactIterator,
                           (BufferedSource, Option[Context], Option[BufferedSource]) => FactIterator],
                         contextOption : Option[Context] = None,
                         schemaFullFilename: Option[String] = None): Unit = {

    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator =
      if (readerEither.isLeft)
        readerEither.left.get(file)
      else {
        val reader = readerEither.right.get
        if (schemaFullFilename.isEmpty)
          reader(file, contextOption, None)
        else
          reader(file, contextOption, Some(scala.io.Source.fromFile(schemaFullFilename.get)))
      }

    factIterator.foreach(factWithStatus => {
      val (factOption, errorOption) = factWithStatus
      if (factOption.nonEmpty)
        println(factOption.get)
      if (errorOption.nonEmpty)
        println(s"ERROR: In $fullFilename : ${errorOption.get}")
    })
  }

}
