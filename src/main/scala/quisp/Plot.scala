package quisp

import quisp.highcharts._

/**
 * Created by rodneykinney on 4/14/15.
 */
object Plot extends HighchartsHtmlDisplay with SeriesDataConversions {
  implicit val display = this

  private def api(data: SeriesData,
                  st: SeriesType) = {
    val config = RootConfig(series = Vector(Series(data.points, `type` = st)))
    val base = new GenericChartAPI(config, display)
    if (data.points.forall(p => p.Name.nonEmpty && p.X.isEmpty && p.Y.isEmpty)) {
      base.xAxis.axisType(AxisType.category)
    }
    else {
      base
    }
  }

  def line(data: SeriesData) = api(data, SeriesType.line)

  def area(data: SeriesData) = api(data, SeriesType.area)

  def areaSpline(data: SeriesData) = api(data, SeriesType.areaspline)

  def bar(data: SeriesData) = api(data, SeriesType.bar)

  def column(data: SeriesData) = api(data, SeriesType.column)

  def pie(data: SeriesData) = api(data, SeriesType.pie)

  def scatter(data: SeriesData) = api(data, SeriesType.scatter)

  def histogram(data: SeriesData, numBins: Int = 50) = {
    val config = RootConfig(series = Vector(Series(data.points, `type` = SeriesType.column)))
    new HistogramAPI(config, display, numBins)
  }

  val HAlign = quisp.highcharts.HAlign
  val VAlign = quisp.highcharts.VAlign
  val AxisType = quisp.highcharts.AxisType
  val SeriesType = quisp.highcharts.SeriesType
  val Orientation = quisp.highcharts.Orientation
  val Stacking = quisp.highcharts.Stacking
  val DashStyle = quisp.highcharts.DashStyle
  val MarkerSymbol = quisp.highcharts.MarkerSymbol
}


