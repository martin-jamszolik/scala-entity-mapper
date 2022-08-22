val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-entity-mapper",
    version := "0.1.0-SNAPSHOT",
    organization := "org.viablespark.mapper",

    scalaVersion := scala3Version,
    scalacOptions += "-rewrite",

    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
    libraryDependencies += "org.glassfish" % "jakarta.el" % "4.0.2",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )

