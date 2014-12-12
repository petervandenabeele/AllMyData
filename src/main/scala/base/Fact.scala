/**
 * Created by peter_v on 21/11/14.
 */

package base

import java.util.UUID
import java.time.{ZoneId, ZonedDateTime}

import common._

// TODO Fix the timestamp to have more digits and/or be monotonic
case class Fact (timeStamp: ATD_TimeStamp = ZonedDateTime.now(ZoneId.of("UTC")).toString,
                 uuid: ATD_Uuid = UUID.randomUUID().toString,
                 context: ATD_Context = "",
                 subject: ATD_Subject = UUID.randomUUID().toString,
                 predicate: ATD_Predicate,
                 objectType: ATD_ObjectType,
                 objectValue: ATD_ObjectValue) {}
