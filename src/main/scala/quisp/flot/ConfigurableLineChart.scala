package quisp.flot

import quisp._
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Specialized line chart
 * @author rodneykinney
 */
class ConfigurableLineChart(
  var config: Chart,
  val display: ChartDisplay[ConfigurableChart[Chart], Int])
  extends LineChartAPI[ConfigurableLineChart]

trait LineChartAPI[T <: UpdatableChart[T, Chart]]
  extends ChartAPI[T] {
  @WebMethod(action = "Data series options")
  override def series(idx: Int) =
    config.series(idx).lineApi(s =>
      update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Line painting options")
  def lineOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val lineOpt = Option(opt.lines).getOrElse(LineChartOptions())
    lineOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(lines = x)))))
  }

  @WebMethod(action = "Marker painting options")
  def markerOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val markerOpt = Option(opt.points).getOrElse(Marker())
    markerOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(points = x)))))
  }
}

case class LineChartOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: LineChartOptions => T) = new LineChartOptionsAPI(this, update)
}

class LineChartOptionsAPI[T](config: LineChartOptions, update: LineChartOptions => T) extends API {
  @WebMethod(action = "Show line")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod
  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill area under line with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

class LineSeriesAPI[T](config: Series, update: Series => T) extends SeriesAPI(config, update) {
  @WebMethod(action = "Line painting options")
  def lineOptions = Option(config.lines).getOrElse(LineChartOptions())
    .api(x => update(config.copy(lines = x)))

  @WebMethod(action = "Marker painting options")
  def markerOptions = Option(config.points).getOrElse(Marker())
    .api(x => update(config.copy(points = x)))

}
