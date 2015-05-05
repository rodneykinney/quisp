package quisp.flot

import quisp._
import quisp.enums._
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * JSON formats for Flot chart config classes
 * @author rodneykinney
 */
object FlotJson {

  import quisp.GeneralJson._


  implicit val markerOptionJS: JsonFormat[Marker] = ExtensibleJsFormat(Marker)
  implicit val lineOptionJS: JsonFormat[LineChartOptions] = ExtensibleJsFormat(LineChartOptions)
  implicit val barOptionJS: JsonFormat[BarChartOptions] = ExtensibleJsFormat(BarChartOptions)
  implicit val pieOptionJS: JsonFormat[PieChartOptions] = ExtensibleJsFormat(PieChartOptions)
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val defaultSeriesOptionsJS: JsonFormat[DefaultSeriesOptions] =
    ExtensibleJsFormat(DefaultSeriesOptions)
  implicit val plotOptionsJS: JsonFormat[ChartOptions] = ExtensibleJsFormat(ChartOptions)

}
