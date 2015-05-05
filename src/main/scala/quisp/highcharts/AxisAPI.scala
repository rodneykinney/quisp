package quisp.highcharts

import quisp.ExtensibleJsObject
import quisp.enums.AxisType
import spray.json.{JsValue, JsonWriter}

import javax.jws.WebMethod

/**
 * @author rodneykinney
 */
case class Axis(
  title: AxisTitle = AxisTitle(),
  `type`: AxisType = AxisType.linear,
  categories: IndexedSeq[String] = null,
  min: Option[Double] = None,
  max: Option[Double] = None,
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject {
  def api[T](update: Axis => T) = new AxisAPI(this)(update)
}

class AxisAPI[T](axis: Axis)(update: Axis => T) extends HcAPI {
  @WebMethod(action="Linear or logarithmic scale")
  def axisType(x: AxisType) = update(axis.copy(`type` = x))

  @WebMethod
  def title = axis.title.api(t => update(axis.copy(title = t)))

  @WebMethod(action="Display category names instead of numerical value")
  def categories(x: String*) = update(axis.copy(categories = x.toIndexedSeq))

  @WebMethod(action="Min/Max value of axis")
  def range(min: Double, max: Double) = update(axis.copy(min = Some(min), max = Some(max)))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(axis.copy(additionalFields = axis.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

}

case class AxisTitle(text: String = "",
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject {
  def api[T](update: AxisTitle => T) = new AxisTitleAPI(this, update)
}

class AxisTitleAPI[T](at: AxisTitle, update: AxisTitle => T) extends HcAPI {
  @WebMethod(action="Axis label")
  def text(x: String) = update(at.copy(text = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(at.copy(additionalFields = at.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}


