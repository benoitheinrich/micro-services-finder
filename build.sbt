lazy val commonSettings = Seq(
  organization := "net.bhservices",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "micro-services-finder"
  )


