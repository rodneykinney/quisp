package quisp.flot

import quisp.enums.FlotSymbol
import quisp.{API, ExtensibleJsObject}
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/28/15.
 */
case class MarkerOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  radius: Option[Int] = None,
  symbol: FlotSymbol = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: MarkerOptions => T) = new MarkerOptionsAPI(this, update)
}

class MarkerOptionsAPI[T](config: MarkerOptions, update: MarkerOptions => T) extends API {
  @WebMethod(action = "Show Marker")
  def show(x: Boolean) = update(config.copy(show = x))

  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill marker outline with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  def radius(x: Int) = update(config.copy(radius = Some(x)))

  def symbol(x: FlotSymbol) = update(config.copy(symbol = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

