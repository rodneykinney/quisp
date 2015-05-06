package quisp.highcharts

import quisp.API
import spray.json.JsonWriter

/**
 * @author rodneykinney
 */
trait HcAPI extends API {

  override def methodDescriptions = {
    val m = super.methodDescriptions
    val special = "additionalField"
    m.filterNot(_.startsWith(special)) ++ m.filter(_.startsWith(special))
  }

  def additionalField[T: JsonWriter](name: String, value: T): Any
}
