import helpers.Evaluator

import org.scalatest._

class EvaluatorSpec extends FlatSpec with Matchers {
  import org.scalatest.OptionValues._

  "Evaluator" should "behave as follows" in {
    Evaluator.eval("3+6").value should be (9)
    Evaluator.eval("3 +6").value should be (9)
    Evaluator.eval("3+     6").value should be (9)
    Evaluator.eval("3 ++6") should be (None)
    Evaluator.eval("") should be (None)
    Evaluator.eval("3 6").value should be (36)
    Evaluator.eval("36").value should be (36)
  }
}
