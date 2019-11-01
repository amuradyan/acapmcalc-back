package helpers

import scala.util.{Failure, Success, Try}

object Evaluator {
  def eval(expression: String): Option[Int] = {
    val normalizedExpression = normalize(expression)
    evaluate(normalizedExpression.split("\\s").toList)
  }

  def normalize(expression: String): String = {
    expression.
      replaceAll(" ", "").
      replaceAll("\\+", " + ").
      replaceAll("-", " - ")
  }

  private def evaluate(expression: List[String]): Option[Int] = expression match {
    case l :: "+" :: r :: rest => {
      val (left, right) = (Try(l.toInt), Try(r.toInt))
      left match {
        case Failure(_) => None
        case Success(l) =>
          right match {
            case Failure(_) => None
            case Success(r) => evaluate((l + r).toString :: rest)
          }
      }
    }
    case l :: "-" :: r :: rest => {
      val (left, right) = (Try(l.toInt), Try(r.toInt))
      left match {
        case Failure(_) => None
        case Success(l) =>
          right match {
            case Failure(_) => None
            case Success(r) => evaluate((l - r).toString :: rest)
          }
      }
    }
    case value :: Nil if value.length > 0 => Some(value.toInt)
    case _ => None
  }
}
