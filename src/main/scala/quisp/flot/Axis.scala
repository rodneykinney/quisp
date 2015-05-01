package quisp.flot

import quisp.enums.AxisMode
import quisp.{API, EscapeLiteral, ExtensibleJsObject}
import spray.json.{JsValue, JsonWriter}

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/28/15.
 */
case class Axis(
  show: Boolean = true,
  mode: AxisMode = null,
  axisLabel: String = null,
  min: Option[Int] = None,
  max: Option[Int] = None,
  tickLabelStyle: Map[String, String] = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Axis => T) = new AxisAPI(this, update)

  def additionalStyle = {
    Option(tickLabelStyle) match {
      case Some(m) if m.nonEmpty =>
        Some(m.toList.map { case (k, v) => s"$k:$v"}
          .mkString(".flot-tick-label {", ";", "}"))
      case _ => None
    }
  }
}

class AxisAPI[T](config: Axis, update: Axis => T) extends API with EscapeLiteral {
  @WebMethod(action="Display the axis")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod(action="Min/Max value of axis")
  def range(min: Int, max: Int) = update(config.copy(min = Some(min), max = Some(max)))

  @WebMethod(action="Show category names instead of numerical value")
  def categorical(x: Boolean) = update(config.copy(mode = if (x) AxisMode.categories else null))

  @WebMethod(action="Axis label")
  def label(x: String) = update(config.copy(axisLabel = x))

  @WebMethod(action="CSS style for axis labels")
  def tickLabelStyle(x: Map[String, String]) = update(config.copy(tickLabelStyle = x))

  @WebMethod(action="Draw axis labels rotated 90 degrees")
  def rotateTickLabels(x: Boolean) = update(config.copy(
    tickLabelStyle = Map("transform" -> "rotate(-90deg)", "text-indent" -> "30px")))

  @WebMethod(action="Use logarithmic scale")
  def logarithmic(x: Boolean) = {
    import spray.json.DefaultJsonProtocol._
    import spray.json._
    if (x) {
      val axisConfig =
        Map("transform" -> literal("function(v) {return Math.log(v);}").toJson,
        "inverseTransform" -> literal("function (v) {return Math.exp(v);}").toJson)
      update(config.copy(additionalFields = (config.additionalFields ++ axisConfig)))
      }
    else {
      update(config.copy(additionalFields = config.additionalFields
        - "transform" - "inverseTransform"))
    }
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}
