package quisp.highcharts

import spray.json.{JsonWriter, JsValue}
import quisp.ExtensibleJsObject

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/18/15.
 */


case class Series(
  data: Seq[Point],
  name: String = "",
  `type`: SeriesType,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Series => T) = new SeriesAPI(this, update)
}

class SeriesAPI[T](series: Series, update: Series => T) extends HcAPI {
  @WebMethod
  def name(s: String) = update(series.copy(name = s))

  @WebMethod
  def seriesType(t: SeriesType) = update(series.copy(`type` = t))

  @WebMethod
  def settings = SeriesSettings().api {
    import HighchartsJson._
    import spray.json._
    newSettings =>
      var o = this.series.additionalFields
      for ((name, value) <- newSettings.toJson.asInstanceOf[JsObject].fields) {
        o += (name -> value)
      }
      update(this.series.copy(additionalFields = o))
  }

  @WebMethod(action = "Draw labels next to individual points in the series")
  def showPointLabels(dataLabel: PointLabelFormat = PointLabelFormat()) = {
    import HighchartsJson._
    additionalField("dataLabels", dataLabel)
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(series.copy(additionalFields = series.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

}

sealed trait Point {
  def X: Option[Double]

  def Y: Option[Double]

  def Name: Option[String]
}

case class RichPoint(name: String = null,
  x: Option[Double] = None,
  y: Option[Double] = None,
  color: Color = null,
  dataLabels: PointLabelFormat = null,
  additionalFields: Map[String, JsValue] = Map()) extends Point with ExtensibleJsObject {
  def X = x

  def Y = y

  def Name = Some(name)
}

case class XYValue(x: Double, y: Double) extends Point {
  def X = Some(x)

  def Y = Some(y)

  def Name = None
}

case class YValue(value: Double) extends Point {
  def X = None

  def Y = Some(value)

  def Name = None
}

case class PointLabelFormat(backgroundColor: Color = null,
  color: Color = null,
  style: Map[String, String] = null,
  verticalAlign: VAlign = null,
  x: Option[Int] = None,
  y: Option[Int] = None,
  borderRadius: Option[Int] = None,
  borderWidth: Option[Int] = None,
  borderColor: Color = null,
  rotation: Option[Int] = None,
  align: HAlign = null,
  enabled: Boolean = true,
  additionalFields: Map[String, JsValue] = Map()) extends ExtensibleJsObject



