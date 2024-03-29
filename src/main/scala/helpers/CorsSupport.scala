package helpers

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`, _}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import com.typesafe.config.ConfigFactory
trait CORSHandler{
  private val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`("Authorization",
      "Content-Type", "X-Requested-With")
  )
  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(corsResponseHeaders)
  }
  //this handles preflight OPTIONS requests.
  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }
  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }
  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCORSHeaders(response: HttpResponse):HttpResponse =
    response.withHeaders(corsResponseHeaders)
}
//trait CorsSupport {
//
//  lazy val allowedOrigins = {
//    val config = ConfigFactory.load()
//    config.getStringList("cors.allowed-origins")
//  }
//
//  //this directive adds access control headers to normal responses
//  private def addAccessControlHeaders(httpOrigin: HttpOrigin) = {
//    var replyOrigin = httpOrigin
//    val httpOriginStr = httpOrigin.scheme + "://" + httpOrigin.host.toString().replaceAll("Host: ", "")
//
//    if (!allowedOrigins.contains(httpOriginStr))
//      replyOrigin = HttpOrigin("http://rafik.com:8888")
//
//    respondWithDefaultHeaders(
//      `Access-Control-Allow-Origin`(replyOrigin),
//      `Access-Control-Allow-Credentials`(true),
//      `Access-Control-Allow-Headers`("Authorization", "Content-Type", "Cache-Control")
//    )
//  }
//
//  //this handles preflight OPTIONS requests.
//  private def preflightRequestHandler(httpOrigin: HttpOrigin): Route = options {
//    var replyOrigin = httpOrigin
//    val httpOriginStr = httpOrigin.scheme + "://" + httpOrigin.host.toString().replaceAll("Host: ", "")
//
//    if (!allowedOrigins.contains(httpOriginStr))
//      replyOrigin = HttpOrigin("http://rafik.com:8888")
//
//    complete(HttpResponse(StatusCodes.OK)
//      .withHeaders(
//        `Access-Control-Allow-Origin`(replyOrigin),
//        `Access-Control-Allow-Methods`(OPTIONS, POST, GET, DELETE, PATCH)))
//  }
//
//  def corsHandler(origin: HttpOrigin)(r: Route) = addAccessControlHeaders(origin) {
//    preflightRequestHandler(origin) ~ r
//  }
//}
