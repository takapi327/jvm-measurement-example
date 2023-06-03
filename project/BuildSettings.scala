import sbt.*
import sbt.Keys.*

import com.lightbend.sbt.javaagent.JavaAgent

object BuildSettings {

  val scalaSettings: Seq[String] = Seq(
    "-Xfatal-warnings",
    "-Wunused:all",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-encoding",
    "utf8",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions"
  )

  val jmxExporterJavaAgent: JavaAgent.AgentModule = {

    val port = 9090
    val conf = "conf/jmx_exporter_conf.yaml"

    val arguments = s"$port:$conf"

    val agent = JavaAgent(
      "io.prometheus.jmx" % "jmx_prometheus_javaagent" % "0.18.0" % "compile;test",
      arguments = arguments
    )

    println(s"jmx_exporter args: '$arguments'")
    println(s"Adding JavaAgent: $agent")
    println(s"JavaAgent.arguments = '${agent.arguments}'")
    println(s"jmx exporter metrics should be available at http://localhost:$port/metrics")

    agent
  }
}
