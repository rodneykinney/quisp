package quisp.flot

import quisp._
import quisp.enums.HAlign
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod


/**
 * Specialized bar chart 
 */
class ConfigurableBarChart(
  var config: Chart,
  val display: ChartDisplay[ConfigurableChart[Chart], Int])
  extends BarChartAPI[ConfigurableBarChart]

trait BarChartAPI[T <: UpdatableChart[T, Chart]]
  extends ChartAPI[T] {
  @WebMethod(action = "Data series options")
  override def series(idx: Int) =
    config.series(idx).barApi(s =>
      update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Bar plot options")
  def options = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val barOpt = Option(opt.bars).getOrElse(BarChartOptions())
    barOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(bars = x)))))
  }
}

case class BarChartOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  barWidth: Option[Double] = None,
  align: HAlign = HAlign.center,
  horizontal: Option[Boolean] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: BarChartOptions => T) = new BarChartOptionsAPI(this, update)
}

class BarSeriesAPI[T](config: Series, update: Series => T) extends SeriesAPI(config, update) {
  @WebMethod(action = "Bar painting options")
  def options = Option(config.bars).getOrElse(BarChartOptions())
    .api(x => update(config.copy(bars = x)))
}

class BarChartOptionsAPI[T](config: BarChartOptions, update: BarChartOptions => T) extends API {
  @WebMethod(action = "Show bars ")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod
  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill bar with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action = "Width of the bar relative to one unit on the axis scale")
  def fractionalBarWidth(x: Double) = update(config.copy(barWidth = Some(x)))

  @WebMethod(action = "Alignment of bar relative to corresponding value on scale")
  def align(x: HAlign) = update(config.copy(align = x))

  @WebMethod(action="Draw horizontal bars instead of columns")
  def horizontal(x: Boolean) = update(config.copy(horizontal = Some(x)))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}
































































