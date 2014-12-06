/**
 * Created by peter_v on 04/12/14.
 */

package cli

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import cli.FactsInserter.reader

@RunWith(classOf[JUnitRunner])
class FactsInserterSuite extends FunSuite {

  test("Object FactsInserter exists") {
    val insterter = FactsInserter
  }

  test("Object FactsInserter can read a CSV file") {
    val factList = reader("empty_file")
    assert(factList === Nil)
  }
}
