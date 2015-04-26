package quisp.flot

import quisp.{Point, XYValue, YValue}
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * Created by rodneykinney on 4/26/15.
 */
object FlotJson {
  import quisp.GeneralJson._
  implicit val seriesFormat: JsonFormat[Series] = jsonFormat1(Series)

}
