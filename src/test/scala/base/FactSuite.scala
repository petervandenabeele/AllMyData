package base

/**
 * Created by peter_v on 20/11/14.
 */

import common._
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  test("the truth") {
    assert(true === true)
  }

  test("Fact can be created without the default arguments") {
    val fact = Fact(predicate = "atd:first_name")
  }

  test("Fact has a predicate attribute") {
    val fact = Fact(predicate = "atd:first_name")
    val predicate: ATD_Predicate = fact.predicate
    assert(predicate == "atd:first_name")
  }
}

