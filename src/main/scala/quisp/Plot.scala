package quisp

import quisp.enums._

/**
 * Created by rodneykinney on 4/14/15.
 */
object Plot extends quisp.highcharts.HighchartsHtmlDisplay with SeriesDataConversions {

  import quisp.highcharts._

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

  object Flot extends quisp.flot.FlotChartDisplay with SeriesDataConversions {

    import quisp.flot._

    def toSeries(data: SeriesData) = {
      val pts = data.points.zipWithIndex.map {
        case (p, i) => (p.X, p.Y, p.Name) match {
          case (None, Some(y), None) => XYValue(i, y)
          case _ => p
        }
      }
      Vector(Series(pts))
    }

    def line(data: SeriesData) = {
      new FlotLineChart(FlotChart(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(lines =
            LineOptions()))), this)
    }

    def area(data: SeriesData) = {
      new FlotLineChart((FlotChart(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(lines =
            LineOptions(fill = Some(0.6)))))), this)
    }

    def column(data: SeriesData) = {
      new FlotBarChart(FlotChart(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(bars =
            BarOptions()))), this)
    }

    //    def bar(data: SeriesData) = {
    //      new FlotGenericAPI(FlotRootConfig(toSeries(data),
    //        options = PlotOptions(series =
    //          DefaultSeriesOptions(bars =
    //            BarOptions(horizontal = None)))), this)
    //    }

    def pie(data: SeriesData) = {
      val series = for (p <- data.points) yield {
        Series(data = List(YValue(p.Y.get)), label = p.Name.getOrElse(null))
      }
      new FlotPieChart(FlotChart(series.toIndexedSeq,
        options = PlotOptions(series =
          DefaultSeriesOptions(pie = PieOptions()))), this)
    }

    def scatter(data: SeriesData) = {
      new FlotLineChart(FlotChart(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(points = MarkerOptions()))), this)
    }

  }


}



