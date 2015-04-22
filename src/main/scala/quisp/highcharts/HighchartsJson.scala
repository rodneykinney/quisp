package quisp.highcharts

import java.awt.Color
import java.lang.reflect.Modifier
import quisp.{EstensibleJsFormat, ExtensibleJsObject}

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.control.NonFatal

/**
 * Created by rodneykinney on 4/15/15.
 */
object HighchartsJson {
  implicit def writerToFormat[T](writer: JsonWriter[T]) = new JsonFormat[T] {

    import quisp.EstensibleJsFormat._

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
  implicit val chartJS: JsonFormat[Chart] = EstensibleJsFormat.apply(Chart)
  implicit val hAlignJS: JsonFormat[HAlign] = EstensibleJsFormat.asString[HAlign]
  implicit val vAlignJS: JsonFormat[VAlign] = EstensibleJsFormat.asString[VAlign]
  implicit val titleJS: JsonFormat[ChartTitle] = EstensibleJsFormat(ChartTitle)
  implicit val axisTitleJS: JsonFormat[AxisTitle] = EstensibleJsFormat(AxisTitle)
  implicit val axisTypeJS: JsonFormat[AxisType] = EstensibleJsFormat.asString[AxisType]
  implicit val axisJS: JsonFormat[Axis] = EstensibleJsFormat(Axis)
  implicit val exportingJS: JsonFormat[Exporting] = EstensibleJsFormat(Exporting)
  implicit val orientationJS: JsonFormat[Orientation] = EstensibleJsFormat.asString[Orientation]
  implicit val legendTitleJS = jsonFormat2(LegendTitle)
  implicit val legendJS: JsonFormat[Legend] = EstensibleJsFormat(Legend)
  implicit val dataLabelsJS: JsonFormat[PointLabelFormat] = EstensibleJsFormat(PointLabelFormat)
  implicit val richPointJS: JsonFormat[RichPoint] = EstensibleJsFormat(RichPoint)
  implicit val stackingJS: JsonFormat[Stacking] = EstensibleJsFormat.asString[Stacking]
  implicit val dashStyleJS: JsonFormat[DashStyle] = EstensibleJsFormat.asString[DashStyle]
  implicit val markerSymbolJS: JsonFormat[MarkerSymbol] = EstensibleJsFormat.asString[MarkerSymbol]
  implicit val markerJS: JsonFormat[MarkerConfig] = EstensibleJsFormat(MarkerConfig)
  implicit val plotSettingsJS: JsonFormat[SeriesSettings] = EstensibleJsFormat(SeriesSettings)
  implicit val plotOptionsJS: JsonFormat[PlotSpecificSettings] = EstensibleJsFormat(PlotSpecificSettings)
  implicit val dataJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(obj: Point) = obj match {
      case n: XYValue => (n.x, n.y).toJson
      case n: YValue => n.value.toJson
      case p: RichPoint => richPointJS.write(p)
    }
  }
  implicit val seriesTypeJS: JsonFormat[SeriesType] = EstensibleJsFormat.asString[SeriesType]
  implicit val seriesJS: JsonFormat[Series] = EstensibleJsFormat(Series)
  implicit val floatingLabelJS = jsonFormat2(FloatingLabel)
  implicit val floatingLabelsJS = jsonFormat1(FloatingLabels)
  implicit val highchartDataJS: JsonFormat[RootConfig] = EstensibleJsFormat(RootConfig)
}