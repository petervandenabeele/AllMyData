/**
  * Created by peter_v on 22/02/15.
  */

package base

import common.{ATD_Subject, newUUID}

/** A logical representation of all facts for one subject. **/
case class Resource(subject: ATD_Subject = newUUID)
