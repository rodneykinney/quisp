package quisp

/**
 * Top-level plotting functions.
 * Users should only have to import Plot._ to use Quisp in the REPL
 * A set of implicit conversions convert most (x,y) inputs into a SeriesData object
 * @author rodneykinney
 */
object Plot extends quisp.flot.FlotChartDisplay with SeriesDataConversions {

  import quisp.flot._

  /** Line plot */
  def line(data: SeriesData) = {
    new FlotLineChart(FlotChart(toSeries(data),
      options = PlotOptions(series =
        DefaultSeriesOptions(lines =
          LineOptions()))), this)
  }

  /** Area-under-line plot */
  def area(data: SeriesData) = {
    new FlotLineChart((FlotChart(toSeries(data),
      options = PlotOptions(series =
        DefaultSeriesOptions(lines =
          LineOptions(fill = Some(0.6)))))), this)
  }

  /** Column (vertical bar) chart */
  def column(data: SeriesData) = {
    new FlotBarChart(FlotChart(toSeries(data),
      options = PlotOptions(series =
        DefaultSeriesOptions(bars =
          BarOptions()))), this)
  }

  /** Bar (horizontal) chart */
  def bar(data: SeriesData) = {
    new FlotBarChart(FlotChart(toSeries(data),
      options = PlotOptions(series =
        DefaultSeriesOptions(bars =
          BarOptions(horizontal = Some(true))))), this)
  }

  /** Pie chart */
  def pie(data: SeriesData) = {
    val series = for (p <- data.points) yield {
      Series(data = List(YValue(p.Y.get)), label = p.Name.getOrElse(null))
    }
    new FlotPieChart(FlotChart(series.toIndexedSeq,
      options = PlotOptions(series =
        DefaultSeriesOptions(pie = PieOptions()))), this)
  }

  /** Scatter plot */
  def scatter(data: SeriesData) = {
    new FlotLineChart(FlotChart(toSeries(data),
      options = PlotOptions(series =
        DefaultSeriesOptions(points = MarkerOptions()))), this)
  }

  /** Histogram */
  def histogram(data: SeriesData, numBins: Int = 50) = {
    val config = FlotChart(Vector(Series(data.points)))
    new HistogramChart(config, this, numBins)
  }

  def toSeries(data: SeriesData) = {
    val pts = data.points.zipWithIndex.map {
      case (p, i) => (p.X, p.Y, p.Name) match {
        case (None, Some(y), None) => XYValue(i, y)
        case _ => p
      }
    }
    Vector(Series(pts))
  }
}



