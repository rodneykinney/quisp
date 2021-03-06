package quisp.highcharts

import quisp.enums._
import quisp.{ExtensibleJsFormat, GeneralJson, Point}
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * JSON formats for Highcharts config
 * @author rodneykinney
 */
object HighchartsJson {

  import quisp.GeneralJson.{colorJS, writerToFormat}

  implicit val chartJS: JsonFormat[ChartOptions] = ExtensibleJsFormat(ChartOptions)
  implicit val titleJS: JsonFormat[ChartTitle] = ExtensibleJsFormat(ChartTitle)
  implicit val axisTitleJS: JsonFormat[AxisTitle] = ExtensibleJsFormat(AxisTitle)
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val exportingJS: JsonFormat[ExportOptions] = ExtensibleJsFormat(ExportOptions)
  implicit val legendTitleJS = jsonFormat2(LegendTitle)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val dataLabelsJS: JsonFormat[PointLabelFormat] = ExtensibleJsFormat(PointLabelFormat)
  implicit val richPointJS: JsonFormat[RichPoint] = ExtensibleJsFormat(RichPoint)
  implicit val markerJS: JsonFormat[MarkerConfig] = ExtensibleJsFormat(MarkerConfig)
  implicit val plotSettingsJS: JsonFormat[SeriesSettings] = ExtensibleJsFormat(SeriesSettings)
  implicit val plotOptionsJS: JsonFormat[PlotSpecificSettings] = ExtensibleJsFormat(PlotSpecificSettings)
  implicit val dataJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(obj: Point) = obj match {
      case p: RichPoint => richPointJS.write(p)
      case _ => GeneralJson.pointJS.write(obj)
    }
  }
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val floatingLabelJS = jsonFormat2(FloatingLabel)
  implicit val floatingLabelsJS = jsonFormat1(FloatingLabels)
  implicit val highchartDataJS: JsonFormat[Chart] = ExtensibleJsFormat(Chart)
}