package quisp

/**
 * Created by rodneykinney on 4/14/15.
 */
object Plot extends quisp.highcharts.HighchartsHtmlDisplay with SeriesDataConversions {

  import quisp.highcharts._

  private def api(data: SeriesData,
    st: SeriesType) = {
    val config = HcRootConfig(series = Vector(Series(data.points, `type` = st)))
    val base = new HcGenericAPI(config, this)
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
    val config = HcRootConfig(series = Vector(Series(data.points, `type` = SeriesType.column)))
    new HistogramAPI(config, this, numBins)
  }

  val HAlign = quisp.highcharts.HAlign
  val VAlign = quisp.highcharts.VAlign
  val AxisType = quisp.highcharts.AxisType
  val SeriesType = quisp.highcharts.SeriesType
  val Orientation = quisp.highcharts.Orientation
  val Stacking = quisp.highcharts.Stacking
  val DashStyle = quisp.highcharts.DashStyle
  val MarkerSymbol = quisp.highcharts.MarkerSymbol

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
      new FlotGenericAPI(FlotRootConfig(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(lines =
            LineOptions()))), this)
    }

    def area(data: SeriesData) = {
      new FlotGenericAPI((FlotRootConfig(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(lines =
            LineOptions(fill = Some(0.6)))))), this)
    }

    def column(data: SeriesData) = {
      new FlotGenericAPI(FlotRootConfig(toSeries(data),
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
      new FlotGenericAPI(FlotRootConfig(series.toIndexedSeq,
        options = PlotOptions(series =
          DefaultSeriesOptions(pie = PieOptions()))), this)
    }

    def scatter(data: SeriesData) = {
      new FlotGenericAPI(FlotRootConfig(toSeries(data),
        options = PlotOptions(series =
          DefaultSeriesOptions(points = MarkerOptions()))), this)
    }

  }


}



