package quisp.flot

import quisp._
import quisp.flot.FlotJson._
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.xml.Unparsed

import javax.jws.WebMethod

/**
 * Top-level Chart and configuration
 * @author rodneykinney
 */
class ConfigurableGenericChart(
  var config: Chart,
  val display: ChartDisplay[ConfigurableChart[Chart], Int])
  extends ChartAPI[ConfigurableGenericChart]


trait ChartAPI[T <: UpdatableChart[T, Chart]]
  extends UpdatableChart[T, Chart] with API {

  @WebMethod(action = "Title text")
  def title(x: String) = update(config.copy(title = x))

  @WebMethod(action = "CSS Style for title")
  def titleStyle(x: Map[String, String]) = update(config.copy(titleStyle = x))

  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))

  @WebMethod(action = "Data series options")
  def addSeries(data: SeriesData) = {
    update(config.copy(series = config.series ++ Plot.toSeries(data)))
  }

  @WebMethod(action = "Data series options")
  def series(idx: Int) =
    config.series(idx).api(s =>
      update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Legend options")
  def legend = Option(config.options.legend).getOrElse(Legend()).api(l =>
    update(config.copy(options = config.options.copy(legend = l))))

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

case class Chart(
  series: IndexedSeq[Series],
  title: String = "",
  titleStyle: Map[String, String] =
  Map("text-align" -> "center", "font-size" -> "18px", "font-family" -> "sans-serif"),
  options: ChartOptions = ChartOptions(),
  width: Int = 500,
  height: Int = 500
  ) extends EscapeLiteral {
  def html = {
    val titleCSS = titleStyle.map { case (k, v) => s"$k:$v"}.mkString(";")
    val containerId = s"container_${hashCode.toHexString}"
    val customStyle = options.customStyle.map(s => s"#$containerId $s").mkString("\n")
    <div style={titleCSS}>
      {title}
    </div>
      <div id={containerId} style={s"width:${width}px;height:${height}px"}></div>
      <style>
        {customStyle}
      </style>
      <script type="text/javascript">
        $ (function() {{
        $.plot(
        {s"$containerId"}
        ,
        {Unparsed(unescapeLiterals(series.toJson.toString))}
        ,
        {Unparsed(unescapeLiterals(options.toJson.toString))}
        );
        }});
      </script>
  }
}

case class ChartOptions(
  series: DefaultSeriesOptions = null,
  legend: Legend = null,
  xaxis: Axis = null,
  yaxis: Axis = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def customStyle = {
    def axisStyle(a: Axis, label: String) =
      Option(a) match {
        case Some(axis) if axis.additionalStyle.nonEmpty =>
          List(s"$label ${axis.additionalStyle.get}")
        case _ => List()
      }
    axisStyle(xaxis, ".flot-x-axis") ++
      axisStyle(yaxis, ".flot-y-axis")
  }

}

case class DefaultSeriesOptions(
  lines: LineChartOptions = null,
  points: Marker = null,
  bars: BarChartOptions = null,
  pie: PieChartOptions = null,
  stack: Option[Boolean] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject























