package quisp.highcharts

import quisp.{ExtensibleJsObjectAPI, ExtensibleJsObject, Point}
import quisp.enums.{HAlign, HcSeriesType, VAlign}
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Data series configuration
 * @author rodneykinney
 */
case class Series(
  data: Seq[Point],
  name: String = "",
  `type`: HcSeriesType,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Series => T) = new SeriesAPI(this, update)
}

class SeriesAPI[T](series: Series, update: Series => T) extends ExtensibleJsObjectAPI {
  @WebMethod(action="Name of series (shown in legend)")
  def name(s: String) = update(series.copy(name = s))

  @WebMethod(action="Plot type for this data series")
  def seriesType(t: HcSeriesType) = update(series.copy(`type` = t))

  @WebMethod(action="Display settings for this data series")
  def settings = SeriesSettings().api {
    import quisp.highcharts.HighchartsJson._
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
    import quisp.highcharts.HighchartsJson._
    additionalField("dataLabels", dataLabel)
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(series.copy(additionalFields = series.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

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



