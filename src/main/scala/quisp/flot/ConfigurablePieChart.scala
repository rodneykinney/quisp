package quisp.flot

import quisp._
import spray.json.JsValue

import javax.jws.WebMethod

/**
 * Specialized pie chart
 * @author rodneykinney
 */
class ConfigurablePieChart(
  var config: Chart,
  val display: ChartDisplay[ConfigurableChart[Chart], Int])
  extends PieChartAPI[ConfigurablePieChart]

trait PieChartAPI[T <: UpdatableChart[T, Chart]]
  extends ChartAPI[T] {

  @WebMethod(action = "Pie chart options")
  def options = {
    val opt = Option(config.options.series).getOrElse(DefaultSeriesOptions())
    val pieOpt = Option(opt.pie).getOrElse(PieChartOptions())
    pieOpt.api(x =>
      update(config.copy(options = config.options.copy(series = opt.copy(pie = x)))))
  }
}

case class PieChartOptions(
  show: Boolean = true,
  radius: Option[Double] = None,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: PieChartOptions => T) = new PieChartOptionsAPI(this, update)
}

class PieChartOptionsAPI[T](config: PieChartOptions, update: PieChartOptions => T) extends API {
  @WebMethod(action = "Show pie chart")
  def show(x: Boolean) = update(config.copy(show = x))

  @WebMethod(action = "Diameter of pie relative to plot size")
  def radius(x: Double) = update(config.copy(radius = Some(x)))
}

