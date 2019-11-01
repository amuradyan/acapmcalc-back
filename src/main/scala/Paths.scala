import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpOrigin
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import helpers.Evaluator
import persistence.AcapmMongoClient


trait Paths {
  val route = {

    val contentType = ContentTypes.`application/json`
    var origin = HttpOrigin("http://a.com")

    extractRequestContext {
      rc => {
        val originHeader = rc.request.getHeader("Origin")

        if (originHeader.isPresent)
          origin = HttpOrigin(originHeader.get().value())

        pathSingleSlash {
          complete("It's alive!!!")
        } ~
          pathPrefix("values") {
            cors() {
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
    }
  }
}
