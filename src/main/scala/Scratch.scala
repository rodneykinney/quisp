import quisp.Plot._

import scala.util.Random

import java.awt.Color

import quisp._
import quisp.highcharts._

object Scratch {

  val x = (1 to 100).map(_ * .01)
  val y = x.map(t => t * t * t * t)

  def main(args: Array[String]): Unit = {
    //    undoRedo()
    suite()
    histograms
  }

  def undoRedo(): Unit = {
    import Plot._
    val x: Iterable[Double] = 1 to 10
    val xx: Iterable[Double] = Array(1.0, 2.0)
    val s: Iterable[String] = Array("","","")
    val lc = line(1 to 10)
    lc.title.text("Title")
    undo()
    redo()
  }

  def conversions: Unit = {
    line(1 to 10)
    line(1 to 10, 2 to 20)
    line((1 to 10).map(i => (i,i*i)))
    line(1 to 10, (x:Double) => x*x)
    def sq(x: Double): Double = x*x
    line(1 to 10, sq _)
    line(1 to 10,"ABCDEFGHIJ".map(_.toString))
    line("ABCDEFGHIJ".map(_.toString), 1 to 10)
    line("ABCDEFGHIJ".map(_.toString).zip((1 to 10)))
    line((1 to 10).zip("ABCDEFGHIJ".map(_.toString)))
  }

  def histograms: Unit = {
    val h1 = histogram(List(0, 1)).title.text("0,1")
    histogram(0 to 200).title.text("0-201")
    histogram(
      for (i <- 1 to 6; j <- 1 to 6) yield i + j
    ).title.text("2d6")
    val r = new Random()
    histogram((0 to 10000).map(i => r.nextDouble())).title.text("Uniform")
    histogram((0 to 10000).map(i => r.nextDouble() + r.nextDouble())).title.text("Bell curve")
  }

  def suite(): Unit = {
    import Plot._
    columns(3)
    val b = bar(x.take(10))
      .addSeries(x.drop(20).take(10))
      .defaultSettings.stacked
      .series(0).name("Apples")
      .series(1).name("Oranges")
      .addFloatingLabel(300, 300, "Look at me!")
      .legend.title("Fruits")
    column(x.take(10))
      .addSeries(x.drop(20).take(10))
      .defaultSettings.stacked
    val as = areaSpline(x, y)
    val l = line(x, x).xAxis.axisType(AxisType.logarithmic)
    val a = area(x, x)
    val lineChart = line(x, x)
      .title.text("Two curves")
      .addSeries(x, (t: Double) => math.sin(t * 4 * math.Pi))
      .series(0).name("line")
      .series(1).name("sinusoid")
      .xAxis.title.text("Distance")
      .yAxis.range(-1, 1)
      .addXAxis(Axis(title = AxisTitle("Other Axis")))
      .yAxis.title.text("Dollars")
      .xAxis.axisType(AxisType.logarithmic)
      .layout.size(800, 400)
    val x1 = x.take(10) ++ x.drop(80).take(10)
    val lcNA = line(List(1.0, 2.0, 3.0), List("One", "Two", "Three"))
      .xAxis.categories("A", "B", "C")
      .yAxis.categories("Do", "Re", "Mi")
      .series(0).showPointLabels(i => DataLabel(backgroundColor = Color.LIGHT_GRAY))

  }
}
