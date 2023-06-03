import sbt.*

object Dependencies {

  val typesafeConfig = "com.typesafe" % "config" % "1.4.2"

  val http4s: Seq[ModuleID] = Seq(
    "http4s-dsl",
    "http4s-ember-server"
  ).map("org.http4s" %% _ % "0.23.18")

}
