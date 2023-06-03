import cats.effect.*

import com.comcast.ip4s.*

import com.typesafe.config.*

import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder

object Main extends ResourceApp.Forever:

  private val config = ConfigFactory.load()

  private val httpHost = config.getString("http.host")
  private val hostPort = config.getInt("http.port")

  private val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "healthcheck" => Ok("Healthcheck Ok")
    case GET -> Root => Ok("Hello World!")
  }

  override def run(args: List[String]): Resource[IO, Unit] =
    for
      _ <- EmberServerBuilder.default[IO]
        .withHost(Host.fromString(httpHost).getOrElse(host"0.0.0.0"))
        .withPort(Port.fromInt(hostPort).getOrElse(port"9000"))
        .withHttpApp(routes.orNotFound)
        .build
    yield ()
