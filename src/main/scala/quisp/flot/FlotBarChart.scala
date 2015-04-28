package quisp.flot

import quisp._
import quisp.enums.HAlign
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod


class FlotBarChart(
  var config: FlotChart,
  val display: ChartDisplay[ConfigurableChart[FlotChart], Int])
  extends FlotBarAPI[FlotBarChart]

trait FlotBarAPI[T <: UpdatableChart[T, FlotChart]]
  extends FlotRootAPI[T] {
  @WebMethod(action = "Data series options")
  override def series(idx: Int) =
    config.series(idx).barApi(s =>
      update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Bar plot options")
  def options = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val barOpt = Option(opt.bars).getOrElse(BarOptions())
    barOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(bars = x)))))
  }
}

case class BarOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  barWidth: Option[Double] = None,
  align: HAlign = HAlign.center,
  horizontal: Option[Boolean] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: BarOptions => T) = new BarOptionsAPI(this, update)
}

class BarSeriesAPI[T](config: Series, update: Series => T) extends SeriesAPI(config, update) {
  @WebMethod(action = "Bar painting options")
  def options = Option(config.bars).getOrElse(BarOptions())
    .api(x => update(config.copy(bars = x)))
}

class BarOptionsAPI[T](config: BarOptions, update: BarOptions => T) extends API {
  @WebMethod(action = "Show bars ")
  def show(x: Boolean) = update(config.copy(show = x))

  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill bar with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action = "Width of the bar relative to one unit on the axis scale")
  def fractionalBarWidth(x: Double) = update(config.copy(barWidth = Some(x)))

  @WebMethod(action = "Alignment of bar relative to corresponding value on scale")
  def align(x: HAlign) = update(config.copy(align = x))

  def horizontal(x: Boolean) = update(config.copy(horizontal = Some(x)))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}
































































