package quisp.highcharts

import java.awt.Color
import java.lang.reflect.Modifier
import quisp.{CustomJsonFormat, CustomJsonObject}

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.control.NonFatal

/**
 * Created by rodneykinney on 4/15/15.
 */
object HighchartsJson {
  implicit def writerToFormat[T](writer: JsonWriter[T]) = new JsonFormat[T] {

    import quisp.CustomJsonFormat._

    override def write(obj: T): JsValue = writer.write(obj)

    override def read(json: JsValue): T = ???
  }

  implicit val color: JsonFormat[Color] =
    new JsonWriter[Color] {
      def write(c: Color) = c.getAlpha match {
        case 255 => "#%02x%02x%02x".format(c.getRed, c.getGreen, c.getBlue).toJson
        case a => s"rgba(${c.getRed},${c.getGreen},${c.getBlue},${a.toDouble / 255})".toJson
      }
    }
  implicit val chartJS: JsonFormat[Chart] = CustomJsonFormat.apply(Chart)
  implicit val hAlignJS: JsonFormat[HAlign] = CustomJsonFormat.asString[HAlign]
  implicit val vAlignJS: JsonFormat[VAlign] = CustomJsonFormat.asString[VAlign]
  implicit val titleJS: JsonFormat[ChartTitle] = CustomJsonFormat(ChartTitle)
  implicit val axisTitleJS: JsonFormat[AxisTitle] = CustomJsonFormat(AxisTitle)
  implicit val axisTypeJS: JsonFormat[AxisType] = CustomJsonFormat.asString[AxisType]
  implicit val axisJS: JsonFormat[Axis] = CustomJsonFormat(Axis)
  implicit val exportingJS: JsonFormat[Exporting] = CustomJsonFormat(Exporting)
  implicit val orientationJS: JsonFormat[Orientation] = CustomJsonFormat.asString[Orientation]
  implicit val legendTitleJS = jsonFormat2(LegendTitle)
  implicit val legendJS: JsonFormat[Legend] = CustomJsonFormat(Legend)
  implicit val dataLabelsJS: JsonFormat[PointLabelFormat] = CustomJsonFormat(PointLabelFormat)
  implicit val richPointJS: JsonFormat[RichPoint] = CustomJsonFormat(RichPoint)
  implicit val stackingJS: JsonFormat[Stacking] = CustomJsonFormat.asString[Stacking]
  implicit val dashStyleJS: JsonFormat[DashStyle] = CustomJsonFormat.asString[DashStyle]
  implicit val markerSymbolJS: JsonFormat[MarkerSymbol] = CustomJsonFormat.asString[MarkerSymbol]
  implicit val markerJS: JsonFormat[MarkerConfig] = CustomJsonFormat(MarkerConfig)
  implicit val plotSettingsJS: JsonFormat[SeriesSettings] = CustomJsonFormat(SeriesSettings)
  implicit val plotOptionsJS: JsonFormat[PlotSpecificSettings] = CustomJsonFormat(PlotSpecificSettings)
  implicit val dataJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(obj: Point) = obj match {
      case n: XYValue => (n.x, n.y).toJson
      case n: YValue => n.value.toJson
      case p: RichPoint => richPointJS.write(p)
    }
  }
  implicit val seriesTypeJS: JsonFormat[SeriesType] = CustomJsonFormat.asString[SeriesType]
  implicit val seriesJS: JsonFormat[Series] = CustomJsonFormat(Series)
  implicit val floatingLabelJS = jsonFormat2(FloatingLabel)
  implicit val floatingLabelsJS = jsonFormat1(FloatingLabels)
  implicit val highchartDataJS: JsonFormat[RootConfig] = CustomJsonFormat(RootConfig)
}