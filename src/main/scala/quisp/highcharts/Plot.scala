package quisp.highcharts

import quisp.enums.{AxisType, HcSeriesType}
import quisp.{SeriesData, SeriesDataConversions}

/**
 * Created by rodneykinney on 4/30/15.
 */
object Plot extends quisp.highcharts.HighchartsHtmlDisplay with SeriesDataConversions {

  private def api(data: SeriesData,
    st: HcSeriesType) = {
    val config = HcRootConfig(series = Vector(Series(data.points, `type` = st)))
    val base = new HcGenericAPI(config, this)
    if (data.points.forall(p => p.Name.nonEmpty && p.X.isEmpty && p.Y.isEmpty)) {
      base.xAxis.axisType(AxisType.category)
    }
    else {
      base
    }
  }

  def line(data: SeriesData) = api(data, HcSeriesType.line)

  def area(data: SeriesData) = api(data, HcSeriesType.area)

  def areaSpline(data: SeriesData) = api(data, HcSeriesType.areaspline)

  def bar(data: SeriesData) = api(data, HcSeriesType.bar)

  def column(data: SeriesData) = api(data, HcSeriesType.column)

  def pie(data: SeriesData) = api(data, HcSeriesType.pie)

  def scatter(data: SeriesData) = api(data, HcSeriesType.scatter)

  def histogram(data: SeriesData, numBins: Int = 50) = {
    val config = HcRootConfig(series = Vector(Series(data.points, `type` = HcSeriesType.column)))
    new HistogramAPI(config, this, numBins)
  }
}
