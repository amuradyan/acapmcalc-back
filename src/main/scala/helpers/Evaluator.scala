package helpers

object Evaluator {
  def eval(expression: String): Option[Int] = {
    val normalizedExpression = normalize(expression)
    evaluate(normalizedExpression.split("\\s").toList)
  }

  private def normalize(expression: String): String = {
    expression.replaceAll("\\+", " + ").replaceAll("-", " - ")
  }

  private def evaluate(expression: List[String]): Option[Int] = expression match {
    case l :: "+" :: r :: rest => evaluate((l.toInt + r.toInt).toString :: rest)
    case l :: "-" :: r :: rest => evaluate((l.toInt - r.toInt).toString :: rest)
    case value :: Nil => Some(value.toInt)
    case _ => None
  }
}
