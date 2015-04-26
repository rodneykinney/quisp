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

  object Radian extends quisp.radian.RadianHtmlChartDisplay with SeriesDataConversions {
    import quisp.radian._

    def line(data: SeriesData) =
      new RadianGenericAPI(RadianRootConfig(Vector(SeriesConfig(data.points, quisp.radian.SeriesType.line))), this)

    def scatter(data: SeriesData) =
      new RadianGenericAPI(RadianRootConfig(Vector(SeriesConfig(data.points, quisp.radian.SeriesType.scatter))), this)
  }

  object Flot extends quisp.flot.FlotChartDisplay with SeriesDataConversions {
    import quisp.flot._
    def line(data: SeriesData) =
      new FlotGenericAPI(FlotRootConfig(Vector(Series(data.points))), this)

  }


}



