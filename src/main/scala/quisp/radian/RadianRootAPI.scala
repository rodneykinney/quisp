package quisp.radian

import quisp.highcharts.{Point, EnumTrait}
import quisp.{UpdatableChart, ChartDisplay, API, ConfigurableChart}

import scala.xml.transform.{RuleTransformer, RewriteRule}
import scala.xml._

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/22/15.
 */
case class RadianRootConfig(
  series: SeriesConfig,
  title: String = "",
  width: Int = 400,
  height: Int = 400,
  xAxis: AxisConfig = AxisConfig("x"),
  yAxis: AxisConfig = AxisConfig("y"),
  lineOptions: LineOptions = LineOptions(),
  markerOptions: MarkerOptions = MarkerOptions()
  ) {
  def html = {
    val n = <plot
    width={s"$width"}
    height={s"$height"}
    title={s"$title"}>
    </plot>
    val modify =
      xAxis.addAttributes _ andThen
        yAxis.addAttributes _ andThen
        lineOptions.addAttributes _ andThen
        series.insertChildren _
    modify(n)
  }
}

trait RadianRootAPI[T <: UpdatableChart[T, RadianRootConfig]]
  extends UpdatableChart[T, RadianRootConfig] with API {
  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))

  @WebMethod(action = "Chart title")
  def title(x: String) = update(config.copy(title = x))

  @WebMethod(action = "X Axis options")
  def xAxis = config.xAxis.api(a => update(config.copy(xAxis = a)))

  @WebMethod(action = "Y Axis options")
  def yAxis = config.yAxis.api(a => update(config.copy(yAxis = a)))

  @WebMethod(action = "Default paint options for all series")
  def defaultLineOptions =
    config.lineOptions.api(o => update(config.copy(lineOptions = o)))

  @WebMethod(action = "Default paint options for all series")
  def defaultMarkerOptions =
    config.markerOptions.api(o => update(config.copy(markerOptions = o)))
}

class RadianGenericAPI(
  var config: RadianRootConfig,
  val display: ChartDisplay[ConfigurableChart[RadianRootConfig], Int])
  extends RadianRootAPI[RadianGenericAPI]

case class AxisConfig(
  id: String,
  label: String = "",
  range: Option[(Int, Int)] = None,
  axisType: AxisType = null) extends AddElementAttributes {
  def api[T](update: AxisConfig => T) = new AxisAPI(this, update)

  def attributes = {
    var attList = Attribute(null, s"axis-$id-label", label, Null)
    range.foreach { case (min, max) => attList = Attribute(null, s"range-$id", s"$min,$max", attList)}
    Option(axisType).foreach(
      t => attList = Attribute(null, s"axis-$id-transform", t.toString, attList))
    attList
  }
}

trait AddElementAttributes {
  def addAttributes(e: Elem): Elem =
    new RuleTransformer(rewrite(e)).transform(e).head.asInstanceOf[Elem]

  def formatColor(c: Color) = c.getAlpha match {
    case 255 => "#%02x%02x%02x".format(c.getRed, c.getGreen, c.getBlue)
    case a => s"rgba(${c.getRed},${c.getGreen},${c.getBlue},${a.toDouble / 255})"
  }

  def rewrite(target: Elem) = new RewriteRule {
    override def transform(n: Node) = n match {
      case e: Elem if e == target => e % attributes
      case _ => n
    }
  }

  def attributes: MetaData

}

case class SeriesConfig(points: Seq[Point],
  lineOptions: LineOptions = LineOptions()) extends InsertChildren {
  def api[T](update: SeriesConfig => T) = new SeriesAPI(this, update)

  def childElements = {
    val id = s"data_${this.hashCode.toHexString}"
    val e = <lines x={s"[[$id.x]]"} y={s"[[$id.y]]"}></lines>
    val data = <plot-data name={s"$id"} format="csv" cols="x,y">
      {points.zipWithIndex.map {
        case (p, i) => s"${p.X.getOrElse(i)},${p.Y.getOrElse(i)}"
      }.mkString("\n")}
    </plot-data>
    Seq(data, lineOptions.addAttributes(e))
  }
}

class SeriesAPI[T](config: SeriesConfig, update: SeriesConfig => T) extends API {

}

trait InsertChildren {
  def insertChildren(e: Elem): Elem = {
    e.copy(child = e.child ++ childElements)
  }

  def childElements: Seq[Elem]
}

class AxisAPI[T](config: AxisConfig, update: AxisConfig => T) extends API {
  @WebMethod(action = "Axis text label")
  def label(x: String) = update(config.copy(label = x))

  @WebMethod(action = "Axis range")
  def range(min: Int, max: Int) = update(config.copy(range = Some(min, max)))

  @WebMethod(action = "Use log or linear scale")
  def axisType(x: AxisType) = update(config.copy(axisType = x))
}

case class LineOptions(
  strokeColor: Color = null,
  strokeWidth: Option[Int] = None
  ) extends AddElementAttributes {
  def api[T](update: LineOptions => T) = new LineOptionsAPI(this, update)

  def attributes = {
    var attrList: MetaData = Null
    Option(strokeColor).foreach(
      x => attrList = Attribute(null, "stroke", formatColor(x), attrList)
    )
    strokeWidth.foreach(
      x => attrList = Attribute(null, "stroke-width", x.toString, attrList)
    )
    attrList
  }
}

case class MarkerOptions(
  marker: Symbol = null,
  markerSize: Option[Int] = None,
  markerFillColor: Color = null
  ) extends AddElementAttributes {
  def api[T](update: MarkerOptions => T) = new MarkerOptionsAPI(this, update)

  def attributes = {
    var attrList: MetaData = Null
    Option(marker).foreach(
      m => attrList = Attribute(null, "marker", m.toString, attrList))
    markerSize.foreach(
      x => attrList = Attribute(null, "marker-size", x.toString, attrList))
    Option(markerFillColor).foreach(
      x => attrList = Attribute(null, "fill", formatColor(x), attrList))
    attrList
  }
}

class LineOptionsAPI[T](config: LineOptions, update: LineOptions => T) {
  @WebMethod(action = "Color of line")
  def strokeColor(x: Color) = update(config.copy(strokeColor = x))

  @WebMethod(action = "Stroke width in pixelsl")
  def strokeWidth(x: Int) = update(config.copy(strokeWidth = Some(x)))

}

class MarkerOptionsAPI[T](config: MarkerOptions, update: MarkerOptions => T) {
  @WebMethod(action = "Marker symbol")
  def symbol(x: Symbol) = update(config.copy(marker = x))

  @WebMethod(action = "Marker width in pixels")
  def markerSize(x: Int) = update(config.copy(markerSize = Some(x * x)))

  @WebMethod(action = "Marker fill color")
  def markerFillColor(x: Color) = update(config.copy(markerFillColor = x))

}

trait AxisType extends EnumTrait

object AxisType {

  case object linear extends AxisType

  case object log extends AxisType

}

trait Symbol extends EnumTrait

object Symbol {

  case object circle extends Symbol

  case object cross extends Symbol

  case object diamond extends Symbol

  case object square extends Symbol

  case object `triangle-down` extends Symbol

  case object `triangle-up` extends Symbol

}