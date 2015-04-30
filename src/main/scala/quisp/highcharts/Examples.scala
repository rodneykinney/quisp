package quisp.highcharts

/**
 * Created by rodneykinney on 4/30/15.
 */
object Examples {
  def main(args: Array[String]): Unit = {
    quisp.highcharts.Plot.columns(3)
    cityTemperatures
    populationGrowth
    largestCities
    electionResults
    browserShare
    heightVsWeight
    bellCurve
  }

  val femaleData = quisp.Examples.femaleData
  val maleData = quisp.Examples.maleData

  def cityTemperatures = {
    import quisp.highcharts.Plot._
    import quisp.enums._
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

  def populationGrowth = {
    import quisp.highcharts.Plot._
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

  def bellCurve = {
    import quisp.highcharts.Plot._
    val rand = new scala.util.Random()
    def randomSum = (0 until 10).map(i => rand.nextDouble).sum - 5.0
    histogram((0 to 10000).map(i => randomSum))
      .title.text("Central Limit Theorem")
  }

  def largestCities = {
    import quisp.highcharts.Plot._
    import quisp.enums._
    import spray.json.DefaultJsonProtocol._

    import java.awt.Color
    val data = List(
      ("Shanghai", 23.7),
      ("Lagos", 16.1),
      ("Instanbul", 14.2),
      ("Karachi", 14.0),
      ("Mumbai", 12.5),
      ("Moscow", 12.1),
      ("São Paulo", 11.8),
      ("Beijing", 11.7),
      ("Guangzhou", 11.1),
      ("Delhi", 11.1),
      ("Shenzhen", 10.5),
      ("Seoul", 10.4),
      ("Jakarta", 10.0),
      ("Kinshasa", 9.3),
      ("Tianjin", 9.3),
      ("Tokyo", 9.0),
      ("Cairo", 8.9),
      ("Dhaka", 8.9),
      ("Mexico City", 8.9),
      ("Lima", 8.9)
    )
    column(data)
      .title.text("World's largest cities")
      .legend.enabled(false)
      .xAxis.axisType(AxisType.category)
      .xAxis.additionalField("labels", Map("rotation" -> -45))
      .yAxis.title.text("Population")
      .series(0).showPointLabels(
        quisp.highcharts.PointLabelFormat(
          borderColor = Color.black,
          color = Color.WHITE,
          rotation = Some(-90),
          align = HAlign.right,
          x = Some(4),
          y = Some(3)
        ))
  }

  def electionResults = {
    import quisp.highcharts.Plot._
    import quisp.enums._

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
    import quisp.highcharts.Plot._
    import spray.json.DefaultJsonProtocol._
    import spray.json._
    val data = List(
      ("Firefox", 45.0),
      ("IE", 26.8),
      ("Chrome", 12.8),
      ("Safari", 8.5),
      ("Opera", 6.2),
      ("Others", 0.7)
    )
    pie(data)
      .title.text("Browser market share")
      .series(0).showPointLabels(
        quisp.highcharts.PointLabelFormat(additionalFields =
          Map("format" -> "{point.name}: {point.percentage:.1f} %".toJson)))
  }

  def heightVsWeight = {
    import quisp.highcharts.Plot._
    import quisp.enums._

    import java.awt.Color

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
      .series(0).settings.color(new Color(223, 83, 83, 128))
      .addSeries(maleData)
      .series(1).name("Male")
      .series(1).settings.color(new Color(119, 152, 191, 128))
  }
}