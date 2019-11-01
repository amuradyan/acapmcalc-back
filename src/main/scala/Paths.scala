import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

trait Paths {
  val route = {
    val contentType = ContentTypes.`application/json`

    pathSingleSlash {
      complete("It's alive!!!")
    }
  }
}
