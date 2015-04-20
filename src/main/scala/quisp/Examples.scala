package quisp

import quisp.highcharts.PointLabelFormat

import scala.util.Random

/**
 * Created by rodneykinney on 4/19/15.
 */
object Examples {


  def cityTemperatures: Unit = {
    import quisp.Plot._
    line(List(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6))
      .xAxis.categories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
      .series(0).name("Tokyo")
      .addSeries(List(-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5))
      .series(1).name("New York")
      .addSeries(List(-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0))
      .series(2).name("Berlin")
      .series(2).showPointLabels()
      .addSeries(List(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
      .series(3).name("London")
      .legend.layout(Orientation.vertical)
      .legend.verticalJustification(VAlign.middle)
      .legend.horizontalJustification(HAlign.right)
      .title.text("Monthly Average Temperatures")
      .yAxis.title.text("Temperature")
  }

  def populationGrowth: Unit = {
    import quisp.Plot._
    area(List(502, 635, 809, 947, 1402, 3634, 5268))
      .yAxis.title.text("Millions")
      .title.text("Worldwide population by Region")
      .xAxis.categories("1750", "1800", "1850", "1900", "1950", "1999", "2050")
      .series(0).name("Asia")
      .addSeries(List(106, 107, 111, 133, 221, 767, 1766))
      .series(1).name("Europe")
      .addSeries(List(163, 203, 276, 408, 547, 729, 628))
      .series(2).name("Africa")
      .addSeries(List(18, 31, 54, 156, 339, 818, 1201))
      .series(3).name("America")
      .addSeries(List(2, 2, 2, 6, 13, 30, 46))
      .series(4).name("Oceana")
      .defaultSettings.stacked
      .defaultSettings.lineWidth(1)
  }

  def bellCurve: Unit = {
    import quisp.Plot._
    val rand = new scala.util.Random()
    def randomSum = (0 until 10).map(i => rand.nextDouble).sum
    histogram((0 to 10000).map(i => randomSum))
  }

  def main(args: Array[String]): Unit = {
    cityTemperatures
    populationGrowth
  }

}
