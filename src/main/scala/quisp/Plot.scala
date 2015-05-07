package quisp

/**
 * Top-level plotting functions.
 * Users should only have to import quisp.Plot._ to use Quisp in the REPL
 * A set of implicit conversions convert most (x,y) inputs into a SeriesData object
 * @author rodneykinney
 */
object Plot extends quisp.flot.FlotChartDisplay with SeriesDataConversions {

  import quisp.flot._

  /** Line plot */
  def line(data: SeriesData) = {
    new ConfigurableLineChart(Chart(toSeries(data),
      options = ChartOptions(series =
        DefaultSeriesOptions(lines =
          LineChartOptions()))), this)
  }

  /** Area-under-line plot */
  def area(data: SeriesData) = {
    new ConfigurableLineChart((Chart(toSeries(data),
      options = ChartOptions(series =
        DefaultSeriesOptions(lines =
          LineChartOptions(fill = Some(0.6)))))), this)
  }

  /** Column (vertical bar) chart */
  def column(data: SeriesData) = {
    new ConfigurableBarChart(Chart(toSeries(data),
      options = ChartOptions(series =
        DefaultSeriesOptions(bars =
          BarChartOptions()))), this)
  }

  /** Bar (horizontal) chart */
  def bar(data: SeriesData) = {
    new ConfigurableBarChart(Chart(toSeries(data),
      options = ChartOptions(series =
        DefaultSeriesOptions(bars =
          BarChartOptions(horizontal = Some(true))))), this)
  }

  /** Pie chart */
  def pie(data: SeriesData) = {
    val series = for (p <- data.points) yield {
      Series(data = List(YValue(p.Y.get)), label = p.Name.getOrElse(null))
    }
    new ConfigurablePieChart(Chart(series.toIndexedSeq,
      options = ChartOptions(series =
        DefaultSeriesOptions(pie = PieChartOptions()))), this)
  }

  /** Scatter plot */
  def scatter(data: SeriesData) = {
    new ConfigurableLineChart(Chart(toSeries(data),
      options = ChartOptions(series =
        DefaultSeriesOptions(points = Marker()))), this)
  }

  /** Histogram */
  def histogram(data: SeriesData, numBins: Int = 50) = {
    val config = Chart(Vector(Series(data.points)))
    new ConfigurableHistogram(config, this, numBins)
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



