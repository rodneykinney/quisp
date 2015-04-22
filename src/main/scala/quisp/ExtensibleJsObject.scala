package quisp

import spray.json.JsValue

/**
 * Created by rodneykinney on 4/18/15.
 */
trait ExtensibleJsObject extends Product {
  val additionalFields: Map[String, JsValue]
}
