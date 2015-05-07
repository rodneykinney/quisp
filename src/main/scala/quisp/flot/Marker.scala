package quisp.flot

import quisp.enums.FlotSymbol
import quisp.{ExtensibleJsObjectAPI, API, ExtensibleJsObject}
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Configuration for rendering points in a line or scatter plot
 * @author rodneykinney
 */
case class Marker(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  radius: Option[Int] = None,
  symbol: FlotSymbol = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Marker => T) = new MarkerAPI(this, update)
}

class MarkerAPI[T](config: Marker, update: Marker => T) extends ExtensibleJsObjectAPI {
  @WebMethod(action = "Show Marker")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod
  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill marker outline with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action="Size in pixels of symbos")
  def radius(x: Int) = update(config.copy(radius = Some(x)))

  @WebMethod(action="Symbol for drawing points")
  def symbol(x: FlotSymbol) = update(config.copy(symbol = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

