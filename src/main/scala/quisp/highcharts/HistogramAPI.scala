package quisp.highcharts

import quisp.enums.HcSeriesType
import quisp._
import spray.json.DefaultJsonProtocol._
import spray.json._

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/18/15.
 */
class HistogramAPI(var config: HcRootConfig,
  val display: ChartDisplay[ConfigurableChart[HcRootConfig], Int],
  numBins: Int) extends HcRootAPI[HistogramAPI] {
  require(config.series.size == 1, "Can only compute histogram from a single series")
  private val originalData =
    for (p <- config.series(0).data) yield (p.X, p.Y) match {
      case (Some(x), None) => x
      case (None, Some(y)) => y
      case _ => sys.error("Single-value series required")
    }
  update(config.copy(series =
    Vector(Series(Histogram.bin(originalData, numBins).points, `type` = HcSeriesType.column))))
  legend.enabled(false)
  this.additionalField("plotOptions",
    Map("column" ->
      Map("pointPadding" -> 0.toJson,
        "borderWidth" -> 0.toJson,
        "groupPadding" -> 0.toJson,
        "shadow" -> false.toJson
      )
    ))

  @WebMethod(action = "Number of histogram bins")
  def bins(numBins: Int) = {
    val binnedData: SeriesData = Histogram.bin(originalData, numBins)
    update(config.copy(series = Vector(Series(binnedData.points, `type` = HcSeriesType.column))))
  }
}



