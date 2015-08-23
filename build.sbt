name := "AllMyData"

version := "0.0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  // The excludes of jms, jmxtools and jmxri are required as per https://issues.apache.org/jira/browse/KAFKA-974.
  // The exclude of slf4j-simple is because it overlaps with our use of logback with slf4j facade; without the exclude
  // we get slf4j warnings and logback's configuration is not picked up.
    "org.apache.kafka" % "kafka_2.11" % "0.8.2.1"
      exclude("javax.jms", "jms")
      exclude("com.sun.jdmk", "jmxtools")
      exclude("com.sun.jmx", "jmxri")
      exclude("org.slf4j", "slf4j-simple")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.0.1"

libraryDependencies += "com.h2database" % "h2" % "1.4.187"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11"