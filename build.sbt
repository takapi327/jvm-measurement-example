import BuildSettings.*

ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.3.0"
ThisBuild / startYear    := Some(2023)

lazy val root = (project in file("."))
  .settings(name := "jvm-measurement-example")
  .settings(javaAgents ++= Seq(jmxExporterJavaAgent))
  .enablePlugins(JavaAgent)
