/**
 * Created by peter_v on 11/03/15.
 */

package base

import java.util.UUID

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ContextSuite extends FunSuite {

  trait testContexts {
    val contextNone = Context(None)
    val contextSome = Context(Some(UUID.randomUUID()))
  }

  test("Context None prints a empty string") {
    new testContexts {
      assertResult("")(contextNone.toString())
    }
  }

  test("Context Some prints a 36 char string") {
    new testContexts {
      assertResult(36)(contextSome.toString().size)
    }
  }

  test("Context#fromString with empty string") {
    new testContexts {
      assertResult(contextNone)(Context(""))
    }
  }

  test("Context#fromString with UUID string") {
    new testContexts {
      assertResult(contextSome)(Context(contextSome.toString()))
    }
  }
}
