package quisp.flot

import quisp._
import quisp.highcharts.HAlign
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * Created by rodneykinney on 4/26/15.
 */
object FlotJson {

  import quisp.GeneralJson._



  implicit val symbolJS: JsonFormat[Symbol] = ExtensibleJsFormat.asString[Symbol]
  implicit val cornerJS: JsonFormat[Corner] = ExtensibleJsFormat.asString[Corner]
  implicit val hAlignJS: JsonFormat[HAlign] = ExtensibleJsFormat.asString[HAlign]
  implicit val markerOptionJS: JsonFormat[MarkerOptions] = ExtensibleJsFormat(MarkerOptions)
  implicit val lineOptionJS: JsonFormat[LineOptions] = ExtensibleJsFormat(LineOptions)
  implicit val barOptionJS: JsonFormat[BarOptions] = ExtensibleJsFormat(BarOptions)
  implicit val pieOptionJS: JsonFormat[PieOptions] = ExtensibleJsFormat(PieOptions)
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val axisModeJS: JsonFormat[AxisMode] = ExtensibleJsFormat.asString[AxisMode]
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val defaultSeriesOptionsJS: JsonFormat[DefaultSeriesOptions] =
    ExtensibleJsFormat(DefaultSeriesOptions)
  implicit val plotOptionsJS: JsonFormat[PlotOptions] = ExtensibleJsFormat(PlotOptions)

}
