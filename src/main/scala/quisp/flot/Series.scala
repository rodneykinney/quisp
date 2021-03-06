package quisp.flot

import quisp.{API, ExtensibleJsObject, Point}
import spray.json.JsValue

import java.awt.Color
import javax.jws.WebMethod

/**
 * Configuration for rendering a data series
 * @author rodneykinney
 */
case class Series(
  data: Seq[Point],
  label: String = null,
  color: Color = null,
  lines: LineChartOptions = null,
  points: Marker = null,
  bars: BarChartOptions = null,
  pie: PieChartOptions = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Series => T) = new SeriesAPI(this, update)

  def lineApi[T](update: Series => T) = new LineSeriesAPI(this, update)

  def barApi[T](update: Series => T) = new BarSeriesAPI(this, update)
}

class SeriesAPI[T](config: Series, update: Series => T) extends API {
  @WebMethod(action = "Data series name")
  def name(x: String) = update(config.copy(label = x))

  @WebMethod
  def color(x: Color) = update(config.copy(color = x))
}

