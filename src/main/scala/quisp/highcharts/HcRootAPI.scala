package quisp.highcharts

import quisp._
import quisp.enums.HcSeriesType
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * @author rodneykinney
 */
case class HcChart(
    chart: Chart = Chart(),
    colors: Seq[Color] = null,
    exporting: Exporting = Exporting(),
    legend: Legend = Legend(),
    series: IndexedSeq[Series] = Vector(),
    subtitle: ChartTitle = null,
    plotOptions: PlotSpecificSettings = null,
    title: ChartTitle = ChartTitle(),
    labels: FloatingLabels = null,
    xAxis: IndexedSeq[Axis] = Vector(Axis()),
    yAxis: IndexedSeq[Axis] = Vector(Axis()),
    additionalFields: Map[String, JsValue] = Map())
    extends ExtensibleJsObject {
  def html = {
    import spray.json._
    import HighchartsJson._
    val json = scala.xml.Unparsed(this.toJson.toString)
    val containerId = json.hashCode.toHexString
    <div id={s"container$containerId"}></div>
        <script type="text/javascript">
          $ (function()
          {{$(
          {s"'#container$containerId'"}
          ).highcharts(
          {json}
          );}}
          );
        </script>
  }
}


class HcGenericAPI(var config: HcChart,
    val display: ChartDisplay[ConfigurableChart[HcChart], Int])
    extends HcRootAPI[HcGenericAPI]

trait HcRootAPI[T <: UpdatableChart[T, HcChart]]
    extends UpdatableChart[T, HcChart] with HcAPI {

  @WebMethod(action = "Options for the i-th X Axis (if multiple axes present")
  def getXAxis(idx: Int) = {
    val axis: Axis = config.xAxis(idx)
    axis.api { a =>
      update(config.copy(xAxis = config.xAxis.updated(idx, a)))
    }
  }

  @WebMethod(action = "Options for the X Axis")
  def xAxis = getXAxis(0)

  @WebMethod(action = "Options for the i-th Y Axis (if multiple axes present")
  def getYAxis(idx: Int) = {
    val axis: Axis = config.yAxis(idx)
    axis.api { a =>
      update(config.copy(yAxis = config.yAxis.updated(idx, a)))
    }
  }

  @WebMethod(action = "Options for the Y Axis")
  def yAxis = getYAxis(0)

  @WebMethod
  def addXAxis(axis: Axis = Axis()) = update(config.copy(xAxis = config.xAxis :+ axis))

  @WebMethod
  def addYAxis(axis: Axis = Axis()) = update(config.copy(yAxis = config.yAxis :+ axis))

  @WebMethod(action = "Settings that apply to all data series on this chart")
  def defaultSettings = {
    val oldPlotOptions = Option(config.plotOptions).getOrElse(PlotSpecificSettings())
    val series = Option(oldPlotOptions.series).getOrElse(SeriesSettings())
    series.api(s => update(config.copy(plotOptions = oldPlotOptions.copy(series = s))))
  }

  @WebMethod(action = "Size, borders, margins, etc.")
  def layout = config.chart.api(c => update(config.copy(chart = c)))

  @WebMethod(action = "Legend layout")
  def legend = config.legend.api(c => update(config.copy(legend = c)))

  @WebMethod(action = "Export to png, pdf, etc.")
  def exporting = config.exporting.api(e => update(config.copy(exporting = e)))

  @WebMethod(action = "Data series attributes")
  def series(idx: Int) = config.series(idx).api(s => update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Add new data series")
  def addSeries(xyData: SeriesData) = update {
    val oldSeries = config.series
    val seriesType = if (oldSeries.size > 0) oldSeries(0).`type` else HcSeriesType.line
    config.copy(series =
        oldSeries :+ Series(data = xyData.points, `type` = seriesType))
  }

  @WebMethod(action = "Title options")
  def title = config.title.api(t => update(config.copy(title = t)))

  @WebMethod(action = "Default colors for data series")
  def colors(x: Seq[Color]) = update(config.copy(colors = x))

  @WebMethod(action = "Add Text Label at (x,y) with CSS style")
  def addFloatingLabel(x: Int, y: Int, text: String, style: Map[String, String] = Map()) = {
    val oldLabels = Option(config.labels).getOrElse(FloatingLabels(Seq()))
    var fullStyle = style
    fullStyle += ("left" -> s"${x}px")
    fullStyle += ("top" -> s"${y}px")
    update(config.copy(labels = FloatingLabels(oldLabels.items :+ FloatingLabel(text, fullStyle))))
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class Exporting(enabled: Boolean = true,
    additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject {
  def api[T](update: Exporting => T) = new ExportingAPI(this, update)
}

class ExportingAPI[T](e: Exporting, update: Exporting => T) extends HcAPI {
  @WebMethod(action = "Enable export control widget")
  def enabled(x: Boolean) = update(e.copy(enabled = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(e.copy(additionalFields = e.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class FloatingLabels(items: Seq[FloatingLabel])

case class FloatingLabel(html: String, style: Map[String, String])

