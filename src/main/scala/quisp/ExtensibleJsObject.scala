package quisp

import spray.json.JsValue

/**
 * A trick to allow access to the underlying javascript plotting implementation
 * even if the Scala API does not have mappings for every option.
 * JSON configuration can always be added via the additionalFields() Map
 * even if the Scala API does not represent that field.
 * @author rodneykinney
 */
trait ExtensibleJsObject extends Product {
  val additionalFields: Map[String, JsValue]
}

/**
 * Allows insertion of literal javascript code into a JSON object,
 * e.g. {"callback": function(msg) {alert(msg);} can be created via
 * val json = Map("callback",literal("function (msg) {alert(msg);}").toJson
 * val code = unescapeLiteral(json.toString)
 */
trait EscapeLiteral {
  val literalRegex = """"LITERAL-BEGIN\|([^|]*)\|LITERAL-END"""".r
  def literal(s: String) = s"LITERAL-BEGIN|$s|LITERAL-END"
  def unescapeLiterals(s: String) = literalRegex.replaceAllIn(s,"$1")
}
