package quisp.enums

import spray.json.{JsString, JsValue, JsonFormat}

/**
 * Enum classes are represented by implementations of this trait
 * A companion object will contain case objects, one for each valid value
 * The simplified class name of the companion object is returned by toString()
 * @author rodneykinney
 */
trait EnumTrait {
  override def toString = this.getClass.getSimpleName.stripSuffix("$")
}

/**
 * Use toString() to represent enum class instances
 */
object EnumTrait {
  def jsFormat[T <: EnumTrait]: JsonFormat[T] = new JsonFormat[T] {
    def write(data: T) = JsString(data.toString)

    def read(json: JsValue): T = ???
  }
}


































