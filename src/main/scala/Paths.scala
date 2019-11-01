import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{entity, _}
import helpers.Evaluator
import persistence.AcapmMongoClient

trait Paths {
  val route = {
    val contentType = ContentTypes.`application/json`

    pathSingleSlash {
      complete("It's alive!!!")
    } ~
      pathPrefix("values") {
        pathEnd {
          get {
            parameters('expression.as[String]) {
              expression => {
                val trimmedExpression = expression.replaceAll(" ", "")
                if (trimmedExpression.length > 0) {
                  val res = Evaluator.eval(trimmedExpression)
                  res match {
                    case Some(value) => {
                      AcapmMongoClient.storeExpression(Evaluator.normalize(expression), value)
                      complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(contentType, value.toString)))
                    }
                    case None => complete(HttpResponse(StatusCodes.InternalServerError, entity = HttpEntity(contentType, "Something went terribly wrong")))
                  }
                }
                else {
                  complete(HttpResponse(StatusCodes.UnprocessableEntity, entity = HttpEntity(contentType, "The expression is invalid")))
                }
              }
            }
          }
        }
      }
  }
}
