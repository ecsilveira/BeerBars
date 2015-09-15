name := "BBServer"

version := "1.0.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.orientechnologies" % "orientdb-core" % "2.1.0",
  "com.orientechnologies" % "orientdb-client" % "2.1.0",
  "com.orientechnologies" % "orientdb-tools" % "2.1.0",
  "com.codahale.metrics" % "metrics-json" % "3.0.1",
  "com.codahale.metrics" % "metrics-annotation" % "3.0.1",
  "commons-lang" % "commons-lang" % "2.6",
  "commons-collections" % "commons-collections" % "3.2"
)     

play.Project.playJavaSettings
