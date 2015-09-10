name := "BBServer"

version := "1.0.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.orientechnologies" % "orientdb-core" % "2.1.0",
  "com.orientechnologies" % "orientdb-client" % "2.1.0",
  "com.orientechnologies" % "orientdb-tools" % "2.1.0"
)     

play.Project.playJavaSettings
