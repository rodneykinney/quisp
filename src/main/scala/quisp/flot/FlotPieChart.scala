package quisp.flot

import quisp._
import spray.json.JsValue

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/28/15.
 */
class FlotPieChart(
  var config: FlotChart,
  val display: ChartDisplay[ConfigurableChart[FlotChart], Int])
  extends FlotPieAPI[FlotPieChart]

trait FlotPieAPI[T <: UpdatableChart[T, FlotChart]]
  extends FlotRootAPI[T] {

  @WebMethod(action = "Pie chart options")
  def options = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val pieOpt = Option(opt.pie).getOrElse(PieOptions())
    pieOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(pie = x)))))
  }
}

case class PieOptions(
  show: Boolean = true,
  radius: Option[Double] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: PieOptions => T) = new PieOptionsAPI(this, update)
}

class PieOptionsAPI[T](config: PieOptions, update: PieOptions => T) extends API {
  @WebMethod(action = "Show pie chart")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod(action = "Diameter of pie relative to plot size")
  def radius(x: Double) = update(config.copy(radius = Some(x)))
}

