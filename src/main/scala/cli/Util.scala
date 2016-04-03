/**
  * Created by peter_v on 27/03/16.
  */

package cli

import base._
import common.FactWithStatusIterator

import scala.io.BufferedSource

object Util {

  def getFileName(args: Array[String]): (String, Option[String]) = args match {
    case Array(dataFile) => (dataFile, None)
    case Array(dataFile, schemaFile) => (dataFile, Some(schemaFile))
    case _ => throw new RuntimeException("provide a dataFile (and optional schemaFile) to read from")
  }

  def getFullFilename(filename: String, dir: String) = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/facts/$dir/" + filename
  }

  def readFactsFromFile(
                         fullFilename: String,
                         readerEither : Either[
                           BufferedSource => FactWithStatusIterator,
                           (BufferedSource, Option[Context], Option[BufferedSource]) => FactWithStatusIterator],
                         contextOption : Option[Context] = None,
                         schemaFullFilename: Option[String] = None): Iterator[Fact] = {

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

    factIterator.collect({
      case (Some(fact), _) => fact
      case (_, Some(error)) => {
        val predicateObject = PredicateObject.errorPredicateObject(s"ERROR: In $fullFilename : $error")
        Fact(predicateObject = predicateObject)
      }
    })
  }

}
