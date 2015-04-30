package quisp

/**
 * Created by rodneykinney on 4/30/15.
 */
object Histogram extends SeriesDataConversions {
  def bin(data: Seq[Double], numBins: Int): SeriesData = {
    val nBins = math.min(numBins, data.size)
    var min = data.min
    var max = data.max
    var binSize = (max - min) / nBins
    min = min -.5 * binSize
    max = max +.5 * binSize
    binSize = (max - min) / nBins
    def getBin(x: Double) = Math.floor((x - min) / binSize).toInt
    val binCount = data.map(getBin).groupBy(x => x).mapValues(_.size).toMap
    def binCenter(i: Int) = min + (i + 0.5) * binSize
    val binnedData: SeriesData =
      for (i <- 0 until nBins) yield (binCenter(i), binCount.getOrElse(i, 0))
    binnedData
  }
}
