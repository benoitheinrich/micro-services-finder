lazy val commonSettings = Seq(
  organization := "net.bhservices",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.7"
)

// Dependencies
val scopt = "com.github.scopt" %% "scopt" % "3.3.0"
val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "micro-services-finder",
    libraryDependencies ++= Seq(
      scopt,
      scalatest
    ),

    // Test options required to allow circleCI to find generated junit XML test reports.
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),

    // Provides a name of the generated jar
    assemblyJarName in assembly := "micro-services-finder.jar",
    test in assembly := {}
  )


