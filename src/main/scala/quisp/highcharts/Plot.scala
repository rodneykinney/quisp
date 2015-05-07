package quisp.highcharts

import quisp.enums.{AxisType, HcSeriesType}
import quisp.{SeriesData, SeriesDataConversions}

/**
 * Top-level plotting functions.
 * Users should only have to import quisp.highcharts.Plot._ to use Quisp in the REPL
 * A set of implicit conversions convert most (x,y) inputs into a SeriesData object
 * @author rodneykinney
 */
object Plot extends quisp.highcharts.HighchartsHtmlDisplay with SeriesDataConversions {

  private def chart(data: SeriesData,
    st: HcSeriesType) = {
    val config = Chart(series = Vector(Series(data.points, `type` = st)))
    val base = new ConfigurableGenericChart(config, this)
    if (data.points.forall(p => p.Name.nonEmpty && p.X.isEmpty && p.Y.isEmpty)) {
      base.xAxis.axisType(AxisType.category)
    }
    else {
      base
    }
  }

  def line(data: SeriesData) = chart(data, HcSeriesType.line)

  def area(data: SeriesData) = chart(data, HcSeriesType.area)

  def areaSpline(data: SeriesData) = chart(data, HcSeriesType.areaspline)

  def bar(data: SeriesData) = chart(data, HcSeriesType.bar)

  def column(data: SeriesData) = chart(data, HcSeriesType.column)

  def pie(data: SeriesData) = chart(data, HcSeriesType.pie)

  def scatter(data: SeriesData) = chart(data, HcSeriesType.scatter)

  def heatmap(data: SeriesData) = {
    require(data.points.forall(_.Z.nonEmpty),"Must define x, y,  and z")
    chart(data, HcSeriesType.heatmap)
  }

  def histogram(data: SeriesData, numBins: Int = 50) = {
    val config = Chart(series = Vector(Series(data.points, `type` = HcSeriesType.column)))
    new HistogramChart(config, this, numBins)
  }
}
