/**
 * Created by peter_v on 21/11/14.
 */

package base

import java.util.UUID

import common._

case class Fact (timeStamp: ATD_TimeStamp = "",
                 uuid: ATD_Uuid = UUID.randomUUID().toString,
                 subject: ATD_Subject = UUID.randomUUID().toString,
                 predicate: ATD_Predicate,
                 objectType: ATD_ObjectType,
                 objectValue: ATD_ObjectValue) {}
