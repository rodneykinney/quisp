package quisp.flot

import quisp._
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * Created by rodneykinney on 4/26/15.
 */
object FlotJson {

  import quisp.GeneralJson._



  implicit val symbolJS: JsonFormat[Symbol] = ExtensibleJsFormat.asString[Symbol]
  implicit val markerOptionJS: JsonFormat[MarkerOptions] = ExtensibleJsFormat(MarkerOptions)
  implicit val lineOptionJS: JsonFormat[LineOptions] = ExtensibleJsFormat(LineOptions)
  implicit val seriesJS: JsonFormat[Series] = ExtensibleJsFormat(Series)
  implicit val legendJS: JsonFormat[Legend] = ExtensibleJsFormat(Legend)
  implicit val axisModeJS: JsonFormat[AxisMode] = ExtensibleJsFormat.asString[AxisMode]
  implicit val axisJS: JsonFormat[Axis] = ExtensibleJsFormat(Axis)
  implicit val plotOptionsJS: JsonFormat[PlotOptions] = ExtensibleJsFormat(PlotOptions)

}
