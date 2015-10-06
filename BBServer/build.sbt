name := "BBServer"

version := "1.0.0"

scalaVersion := "2.10.3"

Keys.fork := true

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  filters,
  cache,
  "com.orientechnologies" % "orientdb-core" % "2.1.0",
  "com.orientechnologies" % "orientdb-client" % "2.1.0",
  "com.orientechnologies" % "orientdb-tools" % "2.1.0",
  "com.orientechnologies" % "orientdb-enterprise" % "2.1.0",
  "com.orientechnologies" % "orientdb-server" % "2.1.0",
  "com.codahale.metrics" % "metrics-json" % "3.0.1",
  "com.codahale.metrics" % "metrics-annotation" % "3.0.1",
  "commons-lang" % "commons-lang" % "2.6",
  "commons-collections" % "commons-collections" % "3.2"
)     

play.Project.playJavaSettings
