ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.remisiki"
ThisBuild / organizationName := "remisiki"
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xlint:_")

lazy val root = (project in file("."))
	.settings(
		name := "browser-cookies",
		Compile / javaSource := baseDirectory.value / "src"
	)

libraryDependencies ++= Seq(
	"org.xerial" % "sqlite-jdbc" % "3.36.0.3",
	"de.swiesend" % "secret-service" % "1.7.0"
)


// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
