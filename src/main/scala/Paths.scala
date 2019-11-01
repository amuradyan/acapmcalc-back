import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{entity, _}
import helpers.Evaluator

trait Paths {
  val route = {
    val contentType = ContentTypes.`application/json`

    pathSingleSlash {
      complete("It's alive!!!")
    } ~
      pathPrefix("expressions") {
        pathEnd {
          post {
            entity(as[String]) {
              expression => {
                if (expression.length > 0) {
                  val res = Evaluator.eval(expression)
                  complete(HttpResponse(StatusCodes.Created, entity = HttpEntity(contentType, res.toString)))
                }
                else {
                  complete(HttpResponse(StatusCodes.UnprocessableEntity, entity = HttpEntity(contentType, "Invalid username/password")))
                }
              }
            }
          }
        }
      }
  }
}
