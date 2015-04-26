package quisp.flot

import quisp._
import quisp.highcharts.EnumTrait
import spray.json._
import DefaultJsonProtocol._

import scala.xml.Unparsed

import java.awt.Color
import javax.jws.WebMethod

import FlotJson._

/**
 * Created by rodneykinney on 4/26/15.
 */
case class FlotRootConfig(
  series: IndexedSeq[Series],
  title: String = "",
  titleStyle: String = "text-align:center",
  options: PlotOptions = PlotOptions(),
  width: Int = 400,
  height: Int = 400
  ) {
  def html = {
    val containerId = s"container_${hashCode.toHexString}"
    <div style={titleStyle}>
      {title}
    </div>
      <div id={containerId} style={s"width:${width}px;height:${height}px"}></div>
      <script type="text/javascript">
        $ (function() {{
        $.plot(
        {s"$containerId"}
        ,
        {Unparsed(series.toJson.toString)}
        ,
        {Unparsed(options.toJson.toString)}
        );
        }});
      </script>
  }
}

class FlotGenericAPI(
  var config: FlotRootConfig,
  val display: ChartDisplay[ConfigurableChart[FlotRootConfig], Int])
  extends FlotRootAPI[FlotGenericAPI]

trait FlotRootAPI[T <: UpdatableChart[T, FlotRootConfig]]
  extends UpdatableChart[T, FlotRootConfig] with API {

  @WebMethod(action = "Title text")
  def title(x: String) = update(config.copy(title = x))

  @WebMethod(action = "CSS Style for title")
  def titleStyle(x: String) = update(config.copy(titleStyle = x))

  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))

  @WebMethod(action = "Data series options")
  def addSeries(data: SeriesData) = {
    update(config.copy(series = config.series ++ Plot.Flot.toSeries(data)))
  }

  @WebMethod(action = "Chart options")
  def options = config.options.api(o => update(config.copy(options = o)))

  @WebMethod(action = "Data series options")
  def series(idx: Int) =
    config.series(idx).api(s =>
      update(config.copy(series = config.series.updated(idx, s))))
}

case class Series(
  data: Seq[Point],
  label: String = null,
  lines: LineOptions = null,
  points: MarkerOptions = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Series => T) = new SeriesAPI(this, update)
}

class SeriesAPI[T](config: Series, update: Series => T) extends API {
  @WebMethod(action = "Data series name")
  def name(x: String) = update(config.copy(label = x))

  @WebMethod(action = "Line painting options")
  def lineOptions = config.lines.api(x => update(config.copy(lines = x)))

  @WebMethod(action = "Marker painting options")
  def markerOptions = Option(config.points).getOrElse(MarkerOptions()).api(x => update(config.copy
    (points = x)))
}

case class Legend(
  show: Option[Boolean] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Legend => T) = new LegendAPI(this, update)
}

class LegendAPI[T](config: Legend, update: Legend => T) extends API {
  @WebMethod(action = "Show/hide legend")
  def enabled(x: Boolean) = update(config.copy(show = Some(x)))
}

case class PlotOptions(
  lines: LineOptions = null,
  points: MarkerOptions = null,
  legend: Legend = null,
  xaxis: Axis = null,
  yaxis: Axis = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: PlotOptions => T) = new PlotOptionsAPI(this, update)
}

class PlotOptionsAPI[T](config: PlotOptions, update: PlotOptions => T) extends API {
  @WebMethod(action = "Legend options")
  def legend = Option(config.legend).getOrElse(Legend()).api(l => update(config.copy(legend = l)))

  @WebMethod(action = "Line painting options")
  def lineOptions =
    Option(config.lines).getOrElse(LineOptions()).api(l => update(config.copy(lines = l)))

  @WebMethod(action = "Marker painting options")
  def markerOptions =
    Option(config.points).getOrElse(MarkerOptions()).api(l => update(config.copy(points = l)))

  @WebMethod(action = "X axis options")
  def xAxis =
    Option(config.xaxis).getOrElse(Axis()).api(x => update(config.copy(xaxis = x)))

  @WebMethod(action = "Y axis options")
  def yAxis =
    Option(config.yaxis).getOrElse(Axis()).api(x => update(config.copy(yaxis = x)))
}

case class LineOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Boolean] = None,
  fillColor: Color = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: LineOptions => T) = new LineOptionsAPI(this, update)
}

class LineOptionsAPI[T](config: LineOptions, update: LineOptions => T) extends API {
  @WebMethod(action = "Show line")
  def show(x: Boolean) = update(config.copy(show = x))

  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  def fill(x: Boolean) = update(config.copy(fill = Some(x)))

  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class MarkerOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Boolean] = None,
  fillColor: Color = null,
  radius: Option[Int] = None,
  symbol: Symbol = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: MarkerOptions => T) = new MarkerOptionsAPI(this, update)
}

class MarkerOptionsAPI[T](config: MarkerOptions, update: MarkerOptions => T) extends API {
  @WebMethod(action = "Show Marker")
  def show(x: Boolean) = update(config.copy(show = x))

  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  def fill(x: Boolean) = update(config.copy(fill = Some(x)))

  def fillColor(x: Color) = update(config.copy(fillColor = x))

  def radius(x: Int) = update(config.copy(radius = Some(x)))

  def symbol(x: Symbol) = update(config.copy(symbol = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class Axis(
  show: Boolean = true,
  mode: AxisMode = null,
  axisLabel: String = null,
  min: Option[Int] = None,
  max: Option[Int] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Axis => T) = new AxisAPI(this, update)
}

class AxisAPI[T](config: Axis, update: Axis => T) extends API {
  def show(x: Boolean) = update(config.copy(show = x))

  def range(min: Int, max: Int) = update(config.copy(min = Some(min), max = Some(max)))

  def categorical(x: Boolean) = update(config.copy(mode = if (x) AxisMode.categories else null))

  def label(x: String) = update(config.copy(axisLabel = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

sealed trait Symbol extends EnumTrait

object Symbol {

  case object circle extends Symbol

  case object square extends Symbol

  case object triangle extends Symbol

  case object diamond extends Symbol

  case object cross extends Symbol

}

sealed trait AxisMode extends EnumTrait

object AxisMode {

  case object categories extends AxisMode

}

