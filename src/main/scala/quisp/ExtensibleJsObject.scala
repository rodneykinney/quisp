package quisp

import spray.json.JsValue

/**
 * Created by rodneykinney on 4/18/15.
 */
trait ExtensibleJsObject extends Product {
  val additionalFields: Map[String, JsValue]
}

trait EscapeLiteral {
  val literalRegex = """"LITERAL-BEGIN\|([^|]*)\|LITERAL-END"""".r
  def literal(s: String) = s"LITERAL-BEGIN|$s|LITERAL-END"
  def unescapeLiterals(s: String) = literalRegex.replaceAllIn(s,"$1")
}
