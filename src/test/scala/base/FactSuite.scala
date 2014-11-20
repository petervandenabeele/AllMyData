package base

/**
 * Created by peter_v on 20/11/14.
 */

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactSuite extends FunSuite {

  test("the truth") {
    assert(true === true)
  }
}
