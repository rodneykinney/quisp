package quisp.highcharts

import sun.java2d.loops.FillRect.General

import java.awt.Color
import java.lang.reflect.Modifier
import quisp.{GeneralJson, Point, ExtensibleJsFormat, ExtensibleJsObject}

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.control.NonFatal

/**
 * Created by rodneykinney on 4/15/15.
 */
object HighchartsJson {
  import GeneralJson.{writerToFormat, colorJS}

  implicit val chartJS: JsonFormat[Chart] = ExtensibleJsFormat(Chart)
  implicit val hAlignJS: JsonFormat[HAlign] = ExtensibleJsFormat.asString[HAlign]
  implicit val vAlignJS: JsonFormat[VAlign] = ExtensibleJsFormat.asString[VAlign]
  implicit val titleJS: JsonFormat[ChartTitle] = ExtensibleJsFormat(ChartTitle)
  implicit val axisTitleJS: JsonFormat[AxisTitle] = ExtensibleJsFormat(AxisTitle)
  implicit val axisTypeJS: JsonFormat[AxisType] = ExtensibleJsFormat.asString[AxisType]
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val exportingJS: JsonFormat[Exporting] = ExtensibleJsFormat(Exporting)
  implicit val orientationJS: JsonFormat[Orientation] = ExtensibleJsFormat.asString[Orientation]
  implicit val legendTitleJS = jsonFormat2(LegendTitle)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val dataLabelsJS: JsonFormat[PointLabelFormat] = ExtensibleJsFormat(PointLabelFormat)
  implicit val richPointJS: JsonFormat[RichPoint] = ExtensibleJsFormat(RichPoint)
  implicit val stackingJS: JsonFormat[Stacking] = ExtensibleJsFormat.asString[Stacking]
  implicit val dashStyleJS: JsonFormat[DashStyle] = ExtensibleJsFormat.asString[DashStyle]
  implicit val markerSymbolJS: JsonFormat[MarkerSymbol] = ExtensibleJsFormat.asString[MarkerSymbol]
  implicit val markerJS: JsonFormat[MarkerConfig] = ExtensibleJsFormat(MarkerConfig)
  implicit val plotSettingsJS: JsonFormat[SeriesSettings] = ExtensibleJsFormat(SeriesSettings)
  implicit val plotOptionsJS: JsonFormat[PlotSpecificSettings] = ExtensibleJsFormat(PlotSpecificSettings)
  implicit val dataJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(obj: Point) = obj match {
      case p: RichPoint => richPointJS.write(p)
      case _ => GeneralJson.pointJS.write(obj)
    }
  }
  implicit val seriesTypeJS: JsonFormat[SeriesType] = ExtensibleJsFormat.asString[SeriesType]
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val floatingLabelJS = jsonFormat2(FloatingLabel)
  implicit val floatingLabelsJS = jsonFormat1(FloatingLabels)
  implicit val highchartDataJS: JsonFormat[HcRootConfig] = ExtensibleJsFormat(HcRootConfig)
}