import com.amazonaws.regions.{ Region, Regions }

import ReleaseTransformations.*

import BuildSettings.*

ThisBuild / organization := "com.github.takapi327"
ThisBuild / scalaVersion := "3.3.0"
ThisBuild / startYear    := Some(2023)

lazy val root = (project in file("."))
  .settings(name := "jvm-measurement-example")
  .settings(
    run / fork := true,
    scalacOptions ++= scalaSettings,
    javaOptions ++= Seq(
      "-Dconfig.file=conf/env.dev/application.conf",
      "-Dlogback.configurationFile=conf/env.dev/logback.xml"
    ),
    javaAgents ++= Seq(jmxExporterJavaAgent)
  )
  .settings(
    Compile / resourceDirectory := baseDirectory(_ / "conf").value,
    Universal / mappings ++= Seq(
      ((Compile / resourceDirectory).value / "jmx_exporter_conf.yaml") -> "conf/jmx_exporter_conf.yaml"
    ),
  )
  .enablePlugins(
    JavaServerAppPackaging,
    DockerPlugin,
    EcrPlugin,
    JavaAgent
  )

Docker / maintainer := "takahiko.tominaga+aws_takapi327_product_a@nextbeat.net"
dockerBaseImage := "amazoncorretto:11"
Docker / dockerExposedPorts := Seq(9000, 9000)
Docker / daemonUser := "daemon"

Ecr / region := Region.getRegion(Regions.AP_NORTHEAST_1)
Ecr / repositoryName := "jvm-server"
Ecr / repositoryTags ++= Seq(version.value)
Ecr / localDockerImage := (Docker / packageName).value + ":" + (Docker / version).value

releaseVersionBump := sbtrelease.Version.Bump.Bugfix

releaseProcess := {
  Seq[ReleaseStep](
    runClean,
    ReleaseStep(state => Project.extract(state).runTask(Docker / publishLocal, state)._1),
    ReleaseStep(state => Project.extract(state).runTask(Ecr / login, state)._1),
    ReleaseStep(state => Project.extract(state).runTask(Ecr / push, state)._1),
  )
}
