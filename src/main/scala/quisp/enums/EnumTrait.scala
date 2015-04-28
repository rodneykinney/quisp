package quisp.enums

import spray.json.{JsString, JsValue, JsonFormat}

/**
 * Created by rodneykinney on 4/17/15.
 */
trait EnumTrait {
  override def toString = this.getClass.getSimpleName.stripSuffix("$")
}

object EnumTrait {
  def jsFormat[T <: EnumTrait]: JsonFormat[T] = new JsonFormat[T] {
    def write(data: T) = JsString(data.toString)

    def read(json: JsValue): T = ???
  }
}


































