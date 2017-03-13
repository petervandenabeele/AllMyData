/**
  * Created by peter_v on 03/04/16.
  */

package base

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OptionalTimestampSuite extends FunSuite {

  trait testOptionalTimestamps {
    val optionalTimestampNone = OptionalTimestamp(None)
    val optionalTimestampSome = OptionalTimestamp(Some("2016-04-03T09:40:34.123456789Z"))
  }

  test("OptionalTimestamp None prints an empty string") {
    new testOptionalTimestamps {
      assertResult("")(optionalTimestampNone.toString())
    }
  }

  test("OptionalTimestamp Some prints the timestamp string") {
    new testOptionalTimestamps {
      assertResult("2016-04-03T09:40:34.123456789Z") { optionalTimestampSome.toString }
    }
  }

  test("OptionalTimestamp(\"\") with empty string is OptionalTimestamp(None)") {
    new testOptionalTimestamps {
      assertResult(optionalTimestampNone)(OptionalTimestamp(""))
    }
  }

  test("OptionalTimestamp(Some(\"foobar\")) throws exception") {
    intercept[IllegalArgumentException] {
      OptionalTimestamp(Some("foobar"))
    }
  }

  test("isEmpty, isDefined, get delegate to timestamp") {
    new testOptionalTimestamps {
      assertResult(false) {optionalTimestampNone.isDefined}
      assertResult(true) {optionalTimestampNone.isEmpty}

      assertResult(true) {optionalTimestampSome.isDefined}
      assertResult(false) {optionalTimestampSome.isEmpty}
      assertResult("2016-04-03T09:40:34.123456789Z") {optionalTimestampSome.get}
    }
  }

}
