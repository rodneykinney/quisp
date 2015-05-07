package quisp.highcharts

/**
 * @author rodneykinney
 */
object Examples {
  def main(args: Array[String]): Unit = {
    quisp.highcharts.Plot.columns(3)
    //    cityTemperatures
    //    populationGrowth
    //    largestCities
    //    electionResults
    //    browserShare
    //    heightVsWeight
    //    bellCurve
    blobs
  }

  def blobs = {
    import quisp.GeneralJson._
    import quisp.highcharts.Plot._
    import spray.json.DefaultJsonProtocol._
    import spray.json._

    import java.awt.Color

    val (x0, y0) = (.2, .2)
    val (x1, y1) = (.6, .8)
    val max = 100
    val data =
      for {
        i <- 0 until max
        x = i.toDouble / max
        j <- 0 until 100
        y = j.toDouble / max
      } yield {
        val z = math.exp(
          -6 * math.pow(x - .3, 2)
            - 36 * math.pow(y - .2, 2)
        ) -
          math.exp(
            -18 * math.pow(x - .75, 2)
              - 18 * math.pow(y - .75, 2)
          )
        (i, j, z)
      }
    heatmap(data)
      //      .layout.additionalField("type", "heatmap")
      .additionalField("colorAxis", Map(
      "min" -> (-1).toJson,
      "max" -> 1.toJson,
      "stops" -> List((0.0, Color.red), (.5, Color.lightGray), (1.0, Color.blue)).toJson
    )
      )
  }

  def cityTemperatures = {
    import quisp.Examples.Data.CityTemperatures._
    import quisp.enums._
    import quisp.highcharts.Plot._
    line(tokyo)
      .xAxis.categories(months: _*)
      .series(0).name("Tokyo")
      .addSeries(newYork)
      .series(1).name("New York")
      .addSeries(berlin)
      .series(2).name("Berlin")
      .series(2).showPointLabels()
      .addSeries(london)
      .series(3).name("London")
      .legend.layout(Orientation.vertical)
      .legend.verticalJustification(VAlign.middle)
      .legend.horizontalJustification(HAlign.right)
      .title.text("Monthly Average Temperatures")
      .yAxis.title.text("Temperature")
  }

  def populationGrowth = {
    import quisp.Examples.Data.PopulationGrowth._
    import quisp.highcharts.Plot._
    area(asia)
      .yAxis.title.text("Millions")
      .title.text("Worldwide population by Region")
      .xAxis.categories(years: _*)
      .series(0).name("Asia")
      .addSeries(europe)
      .series(1).name("Europe")
      .addSeries(africa)
      .series(2).name("Africa")
      .addSeries(america)
      .series(3).name("America")
      .addSeries(oceana)
      .series(4).name("Oceana")
      .defaultSettings.stacked
      .defaultSettings.lineWidth(1)
  }

  def bellCurve = {
    import quisp.highcharts.Plot._
    val rand = new scala.util.Random()
    def randomSum = (0 until 10).map(i => rand.nextDouble).sum - 5.0
    histogram((0 to 10000).map(i => randomSum))
      .title.text("Central Limit Theorem")
  }

  def largestCities = {
    import quisp.Examples.Data.LargestCities.data
    import quisp.highcharts.Plot._
    import spray.json.DefaultJsonProtocol._

    column(data)
      .title.text("World's largest cities")
      .legend.enabled(false)
      .xAxis.axisType(quisp.enums.AxisType.category)
      .xAxis.additionalField("labels", Map("rotation" -> -45))
      .yAxis.title.text("Population")
      .series(0).showPointLabels(
        quisp.highcharts.PointLabelFormat(
          borderColor = java.awt.Color.black,
          color = java.awt.Color.WHITE,
          rotation = Some(-90),
          align = quisp.enums.HAlign.right,
          x = Some(4),
          y = Some(3)
        ))
  }

  def electionResults = {
    import quisp.enums._
    import quisp.highcharts.Plot._

    import java.awt.Color

    bar(List(210863, 498191, 234846))
      .title.text("Congressional Election Results")
      .layout.spacing(10, 25, 10, 10)
      .defaultSettings.stacking(Stacking.PERCENT)
      .xAxis.categories("Montana", "Oregon", "Ohio")
      .yAxis.title.text("Percent")
      .series(0).settings.color(Color.RED)
      .series(0).name("Republican")
      .addSeries(List(17712, 80214, 0))
      .series(1).name("Other")
      .series(1).settings.color(Color.GREEN)
      .addSeries(List(145601, 744516, 250722))
      .series(2).settings.color(Color.BLUE)
      .series(2).name("Democrat")
  }

  def browserShare = {
    import quisp.Examples.Data.BrowserShare._
    import quisp.highcharts.Plot._
    import spray.json.DefaultJsonProtocol._
    import spray.json._
    pie(data)
      .title.text("Browser market share")
      .series(0).showPointLabels(
        quisp.highcharts.PointLabelFormat(additionalFields =
          Map("format" -> "{point.name}: {point.percentage:.1f} %".toJson)))
  }

  def heightVsWeight = {
    import quisp.Examples.Data.HeightVsWeight._
    import quisp.enums._
    import quisp.highcharts.Plot._

    scatter(femaleData)
      .title.text("Height vs Weight by Gender")
      .xAxis.title.text("Height (cm)")
      .yAxis.title.text("Weight (kg)")
      .legend.layout(Orientation.VERTICAL)
      .legend.horizontalJustification(HAlign.LEFT)
      .legend.position(100, 80)
      .legend.floating(true)
      .legend.verticalJustification(VAlign.TOP)
      .series(0).name("Female")
      .series(0).settings.color(new java.awt.Color(223, 83, 83, 128))
      .addSeries(maleData)
      .series(1).name("Male")
      .series(1).settings.color(new java.awt.Color(119, 152, 191, 128))
  }
}