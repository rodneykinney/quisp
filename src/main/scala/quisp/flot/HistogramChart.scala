package quisp.flot

import quisp._

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/30/15.
 */
class HistogramChart(
  var config: FlotChart,
  val display: ChartDisplay[ConfigurableChart[FlotChart], Int],
  numBins: Int)
  extends FlotRootAPI[HistogramChart] {
  private val originalData =
    for (p <- config.series(0).data) yield (p.X, p.Y) match {
      case (Some(x), None) => x
      case (None, Some(y)) => y
      case _ => sys.error("Single-value series required")
    }
  private val binned = Histogram.bin(originalData, numBins).points
  update(config.copy(
    series = Vector(Series(binned)),
    options = PlotOptions(
      series = DefaultSeriesOptions(
        bars = BarOptions(
          barWidth = Some(calculateBarWidth(binned)))))))

  private def calculateBarWidth(data: Seq[Point]) = {
    data.size match {
      case 0 => 1.0
      case 1 => data.head.X.get * 2
      case _ =>
        val Seq(first, second) = data.take(2)
        second.X.get - first.X.get
    }
  }

  @WebMethod(action = "Number of histogram bins")
  def bins(numBins: Int) = {
    val binned = Histogram.bin(originalData, numBins).points
    update(config.copy(
      series = Vector(Series(binned)),
      options = config.options.copy(
        series = config.options.series.copy(
          bars = config.options.series.bars.copy(
            barWidth = Some(calculateBarWidth(binned)))))))
  }

}
