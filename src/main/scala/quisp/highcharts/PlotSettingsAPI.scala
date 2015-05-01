package quisp.highcharts

import quisp.ExtensibleJsObject
import quisp.enums.{DashStyle, HcSymbol, Stacking}
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/18/15.
 */
case class PlotSpecificSettings(
  area: SeriesSettings = null,
  areaspline: SeriesSettings = null,
  bar: SeriesSettings = null,
  column: SeriesSettings = null,
  line: SeriesSettings = null,
  series: SeriesSettings = null,
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject

case class SeriesSettings(
  stacking: Stacking = null,
  shadow: Option[Boolean] = None,
  color: Color = null,
  dashStyle: DashStyle = null,
  lineWidth: Option[Int] = None,
  marker: MarkerConfig = null,
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject {
  def api[T](update: SeriesSettings => T) = new SeriesSettingsAPI[T](this, update)
}

class SeriesSettingsAPI[T](s: SeriesSettings, update: SeriesSettings => T) extends HcAPI {
  @WebMethod(action="Dash style for lines")
  def dashStyle(x: DashStyle) = update(s.copy(dashStyle = x))

  @WebMethod(action="Line/Bar color")
  def color(x: Color) = update(s.copy(color = x))

  @WebMethod
  def lineWidth(x: Int) = update(s.copy(lineWidth = Some(x)))

  @WebMethod(action="Options for point markers")
  def marker = Option(s.marker).getOrElse(MarkerConfig()).api(m => update(s.copy(marker = m)))

  @WebMethod(action="Display shadow")
  def shadow(x: Boolean) = update(s.copy(shadow = Some(x)))

  @WebMethod(action="Stack with other series (display cumulative values)")
  def stacked = stacking(Stacking.normal)

  @WebMethod(action="Stacking style")
  def stacking(x: Stacking) = update(s.copy(stacking = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(s.copy(additionalFields = s.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

}

case class MarkerConfig(
  enabled: Option[Boolean] = None,
  fillColor: Color = null,
  symbol: HcSymbol = null,
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject {
  def api[T](update: MarkerConfig => T) = new MarkerAPI[T](this, update)
}

class MarkerAPI[T](m: MarkerConfig, update: MarkerConfig => T) extends HcAPI {
  @WebMethod(action="Display point markers")
  def enabled(x: Boolean) = update(m.copy(enabled = Some(x)))

  @WebMethod(action="Marker color")
  def color(x: Color) = update(m.copy(fillColor = x))

  @WebMethod(action="Marker symbol")
  def symbol(x: HcSymbol) = update(m.copy(symbol = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(m.copy(additionalFields = m.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

}

