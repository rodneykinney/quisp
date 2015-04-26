package quisp.highcharts

import sun.java2d.loops.FillRect.General

import java.awt.Color
import java.lang.reflect.Modifier
import quisp.{GeneralJson, Point, EstensibleJsFormat, ExtensibleJsObject}

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.control.NonFatal

/**
 * Created by rodneykinney on 4/15/15.
 */
object HighchartsJson {
  import GeneralJson.{writerToFormat, colorJS}

  implicit val chartJS: JsonFormat[Chart] = EstensibleJsFormat(Chart)
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
      case p: RichPoint => richPointJS.write(p)
      case _ => GeneralJson.pointJS.write(obj)
    }
  }
  implicit val seriesTypeJS: JsonFormat[SeriesType] = EstensibleJsFormat.asString[SeriesType]
  implicit val seriesJS: JsonFormat[Series] = EstensibleJsFormat(Series)
  implicit val floatingLabelJS = jsonFormat2(FloatingLabel)
  implicit val floatingLabelsJS = jsonFormat1(FloatingLabels)
  implicit val highchartDataJS: JsonFormat[HcRootConfig] = EstensibleJsFormat(HcRootConfig)
}