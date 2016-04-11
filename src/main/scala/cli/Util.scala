/**
  * Created by peter_v on 27/03/16.
  */

package cli

import base._
import common._

import scala.io.BufferedSource

object Util {

  def getFileName(args: Array[String]): (String, Option[String], Option[String]) = args match {
    case Array(dataFile) => (dataFile, None, None)
    case Array(dataFile, schemaFile) => (dataFile, Some(schemaFile), None)
    case Array(dataFile, schemaFile, contextFile) => (dataFile, Some(schemaFile), Some(contextFile))
    case _ => throw new RuntimeException("provide a dataFile and optional schemaFile and optional contextFile to read from")
  }

  def getFullFilename(filename: String, dir: String) = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/data/$dir/" + filename
  }

  def handleResults(results: Iterator[Fact]) =
    results.foreach(println)

  def readFactsFromFile(fullFilename: String,
                        readerEither : Either[
                          BufferedSource => FactWithStatusIterator,
                          (BufferedSource, Context, Option[BufferedSource]) => FactWithStatusIterator],
                        context : Context = Context(None),
                        schemaFullFilename: Option[String] = None): Iterator[Fact] = {

    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator =
      if (readerEither.isLeft)
        readerEither.left.get(file)
      else {
        val reader = readerEither.right.get
        if (schemaFullFilename.isEmpty)
          reader(file, context, None)
        else
          reader(file, context, Some(scala.io.Source.fromFile(schemaFullFilename.get)))
      }

    factIterator.collect({
      case (Some(fact), _) => fact
      case (_, Some(error)) => {
        val predicateObject = PredicateObject.errorPredicateObject(s"ERROR: In $fullFilename : $error")
        Fact(predicateObject = predicateObject)
      }
    })
  }

  def contextAndFacts(contextFullFilename: String): (Context, Iterator[Fact]) = {

    val contextFacts = readFactsFromFile(
      fullFilename = contextFullFilename,
      readerEither = Left(csv.InFactsReader.reader)
    ).toSeq

    val subjects = contextFacts.map(fact => fact.subject)
    assert(subjects.distinct.length == 1, "All subjects for this context must be the same")

    val contextSubject: AMD_Subject = subjects.head
    (Context(Some(contextSubject)), contextFacts.toIterator)
  }

}
