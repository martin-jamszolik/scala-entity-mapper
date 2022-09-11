val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-entity-mapper",
    version := "0.1.0-SNAPSHOT",
    organization := "org.viablespark.mapper",

    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-rewrite", "-indent"),

    libraryDependencies += "org.scala-lang.modules" % "scala-xml_3" % "2.1.0",
    libraryDependencies += "org.springframework" % "spring-expression" % "5.3.22",
    libraryDependencies += "org.scalameta" % "munit_3" % "1.0.0-M6" % Test
  )

