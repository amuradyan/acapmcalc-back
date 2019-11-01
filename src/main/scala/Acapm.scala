import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
class Acapm

object Acapm extends App with Paths {
  val logger = Logger[Acapm]

  val conf = ConfigFactory.load()
  val host = conf.getString("app.host")
  val port = conf.getInt("app.port")

  implicit val actorSystem = ActorSystem("Acapm")
  implicit val executionCtx = actorSystem.dispatcher
  implicit val materializer = ActorMaterializer()

  val bindingFuture: Future[ServerBinding] = null

  val f = for {_ <- Http().bindAndHandle(route, host, port)
               waitOnFuture <- Promise[Done].future
  } yield waitOnFuture

  logger.info(s"Server online at https://$host:$port/")

  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }

  Await.ready(f, Duration.Inf)
}
