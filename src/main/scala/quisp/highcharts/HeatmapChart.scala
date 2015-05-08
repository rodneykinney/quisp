package quisp.highcharts

import quisp.highcharts.HighchartsJson._
import quisp.{ChartDisplay, ConfigurableChart, ExtensibleJsObject, ExtensibleJsObjectAPI}
import spray.json._

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 5/7/15.
 */
class HeatmapChart(var config: Chart,
  val display: ChartDisplay[ConfigurableChart[Chart], Int])
  extends ChartAPI[HeatmapChart] {
  require(config.series.forall(_.data.forall(_.Z.nonEmpty)), "Must define x, y,  and z")
  private val (zMin, zMax) =
    config.series.flatMap(_.data).foldLeft((Double.MaxValue, Double.MinValue)) {
      case ((min, max), p) => (math.min(min, p.Z.get), math.max(max, p.Z.get))
    }

  private var colorAxis = ColorAxis(min = zMin, max = zMax)

  update(config.copy(additionalFields =
    config.additionalFields +
      ("colorAxis" -> colorAxis.toJson)))

  @WebMethod(action = "Heatmap color options")
  def heatmapColors = {
    colorAxis.api(x => {
      colorAxis = x
      update(config.copy(additionalFields = config.additionalFields +
        ("colorAxis" -> colorAxis.toJson)))
    })
  }
}

case class ColorAxis(
  min: Double,
  max: Double,
  minColor: Color = null,
  maxColor: Color = null,
  stops: Seq[(Double, Color)] = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: ColorAxis => T) = new ColorAxisAPI(this, update)
}

class ColorAxisAPI[T](config: ColorAxis, update: ColorAxis => T) extends ExtensibleJsObjectAPI {
  @WebMethod(action = "Color of minimum value")
  def minColor(x: Color) = update(config.copy(minColor = x))

  @WebMethod(action = "Color of maximum value")
  def maxColor(x: Color) = update(config.copy(maxColor = x))

  @WebMethod(action = "Fix color of specific value.  Other values will be interpolated.")
  def fixColor(value: Double, c: Color) = {
    val oldStops =
      Option(config.stops) match {
        case Some(s) => s.toVector
        case None =>
          val minColor = Option(config.minColor).getOrElse(Color.decode("0xEFFFFF"))
          val maxColor = Option(config.maxColor).getOrElse(Color.decode("0x102D4C"))
          Vector((0.0, minColor), (1.0, maxColor))
      }
    val newStops =
      (oldStops :+(((value - config.min) / (config.max - config.min), c))).sortBy(_._1)
    update(config.copy(stops = newStops))
  }

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(config.copy(additionalFields = config.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}
