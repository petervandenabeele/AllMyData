/**
  * Created by peter_v on 22/02/15.
  */

package base

import common.{AMD_Subject, newUUID}

/** A logical representation of all facts for one subject. */
case class Resource(subject: AMD_Subject = newUUID)
