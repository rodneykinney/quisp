package quisp.flot

import quisp._
import quisp.enums._
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * @author rodneykinney
 */
object FlotJson {

  import quisp.GeneralJson._


  implicit val markerOptionJS: JsonFormat[MarkerOptions] = ExtensibleJsFormat(MarkerOptions)
  implicit val lineOptionJS: JsonFormat[LineOptions] = ExtensibleJsFormat(LineOptions)
  implicit val barOptionJS: JsonFormat[BarOptions] = ExtensibleJsFormat(BarOptions)
  implicit val pieOptionJS: JsonFormat[PieOptions] = ExtensibleJsFormat(PieOptions)
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val defaultSeriesOptionsJS: JsonFormat[DefaultSeriesOptions] =
    ExtensibleJsFormat(DefaultSeriesOptions)
  implicit val plotOptionsJS: JsonFormat[PlotOptions] = ExtensibleJsFormat(PlotOptions)

}
