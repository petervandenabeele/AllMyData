/**
  * Created by peter_v on 27/03/16.
  */

package cli

import base._
import common._

import scala.io.BufferedSource

object Util {

  /** Naive: extract filename(s) from arguments
    * FIXME: use proper command line parsing
    *
    * @param args dataFile and optional schemaFile and optional contextFile
    * @return a tuple with 1, 2 or 3 defined filenams
    */
  def getFileName(args: Array[String]): (String, Option[String], Option[String]) = args match {
    case Array(dataFile) => (dataFile, None, None)
    case Array(dataFile, schemaFile) => (dataFile, Some(schemaFile), None)
    case Array(dataFile, schemaFile, contextFile) => (dataFile, Some(schemaFile), Some(contextFile))
    case _ => throw new RuntimeException("provide a dataFile and optional schemaFile and optional contextFile to read from")
  }

  /** Naive: expand to full filename on local system of @peter_v
    *
    * @param filename  short filename (can contain / like in `test/foo_bar`)
    * @param dir  is this in the data or metadata directory
    * @return the full filename
    */
  def getFullFilename(filename: String, dir: String): String = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/data/$dir/" + filename
  }

  /** Naive: just print all results to stdout ; this is used by later scripts
    *
     * @param results this is an Iterator (so large datasets can be processed)
    */
  def handleResults(results: Iterator[Fact]): Unit =
    results.foreach(println)

  /** Read the facts from file using different reader types (supplied in the CLI)
    *
    * @param fullFilename full filename of the data file
    * @param readerEither the reader having one 2 different signatures
    * @param context the context (can be empty if the datafile had built-in context)
    * @param schemaFullFilename the schema (needed for JSON parsing)
    * @return an Iterator (to process large data sets)
    */
  def readFactsFromFile(fullFilename: String,
                        readerEither : Either[
                          BufferedSource => FactWithStatusIterator,
                          (BufferedSource, Context, Option[BufferedSource], Option[String]) => FactWithStatusIterator],
                        context : Context = Context(None),
                        schemaFullFilename: Option[String] = None,
                        factsAtOption: Option[String] = None): Iterator[Fact] = {
    val file = scala.io.Source.fromFile(fullFilename)
    val factIterator =
      if (readerEither.isLeft)
        // fact style (context, schema already in the data)
        readerEither.left.get(file)
      else {
        // event style (context, schema from external files)
        val reader = readerEither.right.get
        val schemaOption =
          if (schemaFullFilename.isEmpty)
            None
          else
            Some(scala.io.Source.fromFile(schemaFullFilename.get))
        reader(file, context, schemaOption, factsAtOption)
      }

    factIterator.collect({
      case (Some(fact), _) => fact
      case (_, Some(error)) =>
        val predicateObject = PredicateObject.errorPredicateObject(s"ERROR: In $fullFilename : $error")
        Fact(predicateObject = predicateObject)
    })
  }

  /** Read the context with its facts from a context file
    *
    * @param contextFullFilename filename of the context file (should be of in_facts format)
    * @return A tuple with a context and a fresh iterator over the facts inside
    */
  def contextAndFacts(contextFullFilename: String): (Context, Iterator[Fact], Option[String]) = {
    val contextFacts = readFactsFromFile(
      fullFilename = contextFullFilename,
      readerEither = Left(csv.InFactsReader.reader)
    ).toSeq

    val subjects = contextFacts.map(fact => fact.subject)
    assert(subjects.distinct.length == 1, "All subjects for this context must be the same")
    val contextSubject: AMD_Subject = subjects.head

    val factsAtOption = contextFacts.find(fact => fact.predicate == "amd:context:facts_at").map(fact => fact.objectValue)
    (Context(Some(contextSubject)), contextFacts.toIterator, factsAtOption)
  }

  /** Get the full filename but also logs it to println while at it
    *
    * @param file the local filename
    * @param directory the directory: data or metadata
    * @param logText the log text to append (try similar length)
    * @return the fullFilename
    */
  def getAndLogFullFileName(file: String, directory: String, logText: String): String = {
    val fullFilename = getFullFilename(file, directory)
    println(s"$logText $fullFilename")
    fullFilename
  }

}
