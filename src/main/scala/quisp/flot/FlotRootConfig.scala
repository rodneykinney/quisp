package quisp.flot

import quisp._
import quisp.highcharts.{HAlign, EnumTrait}
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
  titleStyle: Map[String, String] =
  Map("text-align" -> "center", "font-size" -> "18px", "font-family" -> "sans-serif"),
  options: PlotOptions = PlotOptions(),
  width: Int = 500,
  height: Int = 500
  ) {
  def html = {
    val titleCSS = titleStyle.map { case (k, v) => s"$k:$v"}.mkString(";")
    val containerId = s"container_${hashCode.toHexString}"
    <div style={titleCSS}>
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
  def titleStyle(x: Map[String, String]) = update(config.copy(titleStyle = x))

  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))

  @WebMethod(action = "Data series options")
  def addSeries(data: SeriesData) = {
    update(config.copy(series = config.series ++ Plot.Flot.toSeries(data)))
  }

  @WebMethod(action = "Data series options")
  def series(idx: Int) =
    config.series(idx).api(s =>
      update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Legend options")
  def legend = Option(config.options.legend).getOrElse(Legend()).api(l =>
    update(config.copy(options = config.options.copy(legend = l))))

  @WebMethod(action = "Line painting options")
  def lineOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val lineOpt = Option(opt.lines).getOrElse(LineOptions())
    lineOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(lines = x)))))
  }

  @WebMethod(action = "Marker painting options")
  def markerOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val markerOpt = Option(opt.points).getOrElse(MarkerOptions())
    markerOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(points = x)))))
  }

  @WebMethod(action = "Bar plot options")
  def barOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val barOpt = Option(opt.bars).getOrElse(BarOptions())
    barOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(bars = x)))))
  }

  @WebMethod(action = "Pie chart options")
  def pieOptions = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val pieOpt = Option(opt.pie).getOrElse(PieOptions())
    pieOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(pie = x)))))
  }

  @WebMethod(action = "X axis options")
  def xAxis =
    Option(config.options.xaxis).getOrElse(Axis()).api(x =>
      update(config.copy(options = config.options.copy(xaxis = x))))

  @WebMethod(action = "Y axis options")
  def yAxis =
    Option(config.options.yaxis).getOrElse(Axis()).api(x =>
      update(config.copy(options = config.options.copy(yaxis = x))))

  @WebMethod(action = "Stack data series together")
  def stacked(x: Boolean) = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    update(config.copy(options = config.options.copy(series = opt.copy(stack = Some(x)))))
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(options =
    config.options.copy(additionalFields = config.options.additionalFields +
      (name -> implicitly[JsonWriter[V]].write(value)))))

}

case class Series(
  data: Seq[Point],
  label: String = null,
  color: Color = null,
  lines: LineOptions = null,
  points: MarkerOptions = null,
  bars: BarOptions = null,
  pie: PieOptions = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Series => T) = new SeriesAPI(this, update)
}

class SeriesAPI[T](config: Series, update: Series => T) extends API {
  @WebMethod(action = "Data series name")
  def name(x: String) = update(config.copy(label = x))

  def color(x: Color) = update(config.copy(color = x))

  @WebMethod(action = "Line painting options")
  def lineOptions = Option(config.lines).getOrElse(LineOptions())
    .api(x => update(config.copy(lines = x)))

  @WebMethod(action = "Marker painting options")
  def markerOptions = Option(config.points).getOrElse(MarkerOptions())
    .api(x => update(config.copy(points = x)))

  @WebMethod(action = "Bar painting options")
  def barOptions = Option(config.bars).getOrElse(BarOptions())
    .api(x => update(config.copy(bars = x)))

}

case class Legend(
  show: Option[Boolean] = None,
  labelBoxBorderColor: Color = null,
  noColumns: Option[Int] = None,
  position: Corner = null,
  backgroundColor: Color = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Legend => T) = new LegendAPI(this, update)
}

class LegendAPI[T](config: Legend, update: Legend => T) extends API {
  @WebMethod(action = "Show/hide legend")
  def enabled(x: Boolean) = update(config.copy(show = Some(x)))

  def borderColor(x: Color) = update(config.copy(labelBoxBorderColor = x))

  @WebMethod(action = "Position of Legend")
  def position(x: Corner) = update(config.copy(position = x))

  def backgroundColor(x: Color) = update(config.copy(backgroundColor = x))

  @WebMethod(action = "Number of columns in legend")
  def columns(x: Int) = update(config.copy(noColumns = Some(x)))
}

case class PlotOptions(
  series: DefaultSeriesOptions = null,
  legend: Legend = null,
  xaxis: Axis = null,
  yaxis: Axis = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject

case class DefaultSeriesOptions(
  lines: LineOptions = null,
  points: MarkerOptions = null,
  bars: BarOptions = null,
  pie: PieOptions = null,
  stack: Option[Boolean] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject

case class LineOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
  fillColor: Color = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: LineOptions => T) = new LineOptionsAPI(this, update)
}

class LineOptionsAPI[T](config: LineOptions, update: LineOptions => T) extends API {
  @WebMethod(action = "Show line")
  def show(x: Boolean) = update(config.copy(show = x))

  def lineWidth(x: Int) = update(config.copy(lineWidth = Some(x)))

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill area under line with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class MarkerOptions(
  show: Boolean = true,
  lineWidth: Option[Int] = None,
  fill: Option[Double] = None,
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

  @WebMethod(action = "Opacity of fill color")
  def fillOpacity(x: Double) = update(config.copy(fill = Some(x)))

  @WebMethod(action = "Fill marker outline with this color")
  def fillColor(x: Color) = update(config.copy(fillColor = x))

  def radius(x: Int) = update(config.copy(radius = Some(x)))

  def symbol(x: Symbol) = update(config.copy(symbol = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
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

case class PieOptions(
  show: Boolean = true,
  radius: Option[Double] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: PieOptions => T) = new PieOptionsAPI(this, update)
}

class PieOptionsAPI[T](config: PieOptions, update: PieOptions => T) extends API {
  @WebMethod(action = "Show pie chart")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod(action = "Diameter of pie relative to plot size")
  def radius(x: Double) = update(config.copy(radius = Some(x)))
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

sealed trait Corner extends EnumTrait

object Corner {

  case object ne extends Corner

  case object nw extends Corner

  case object se extends Corner

  case object sw extends Corner

}


