package quisp.radian

import quisp.highcharts.EnumTrait
import quisp.{UpdatableChart, ChartDisplay, API, ConfigurableChart}

import scala.xml.transform.{RuleTransformer, RewriteRule}
import scala.xml._

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/22/15.
 */
case class RadianRootConfig(
  title: String = "",
  width: Int = 400,
  height: Int = 400,
  xAxis: AxisConfig = AxisConfig("x"),
  yAxis: AxisConfig = AxisConfig("y"),
  paintOptions: PaintOptions = PaintOptions()
  ) {
  def html = {
    val n = <plot
    width={s"$width"}
    height={s"$height"}
    title={s"$title"}>
      <lines x="[[seq(0,2*PI,101)]]" y="[[sin(x)]]"></lines>
    </plot>
    val modify =
      xAxis.addAttributes _ andThen
        yAxis.addAttributes _ andThen
        paintOptions.addAttributes _
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
  def defaultPaintOptions =
    config.paintOptions.api(o => update(config.copy(paintOptions = o)))
}

class RadianGenericAPI(
  var config: RadianRootConfig,
  val display: ChartDisplay[ConfigurableChart[RadianRootConfig], Int])
  extends RadianRootAPI[RadianGenericAPI]

case class AxisConfig(
  id: String,
  label: String = "on",
  range: Option[(Int, Int)] = None,
  axisType: AxisType = null) extends AddElementAttributes {
  def api[T](update: AxisConfig => T) = new AxisAPI(this, update)

  def attributes = {
    var attList = Attribute(null, s"axis-$id-label", label, Null)
    range.foreach { case (min, max) => attList = Attribute(null, s"range-$id", s"$min,$max", attList)}
    Option(range).foreach(t => attList = Attribute(null, s"axis-$id-transform", t.toString, attList))
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

class AxisAPI[T](config: AxisConfig, update: AxisConfig => T) extends API {
  @WebMethod(action = "Axis text label")
  def label(x: String) = update(config.copy(label = x))

  @WebMethod(action = "Axis range")
  def range(min: Int, max: Int) = update(config.copy(range = Some(min, max)))
}

case class PaintOptions(
  marker: Marker = null,
  markerSize: Option[Int] = None,
  markerFillColor: Color = null,
  strokeColor: Color = null,
  strokeWidth: Option[Int] = None
  ) extends AddElementAttributes {
  def api[T](update: PaintOptions => T) = new PaintOptionsAPI(this, update)

  def attributes = {
    var attrList: MetaData = Null
    Option(marker).foreach(
      m => attrList = Attribute(null, "marker", m.toString, attrList))
    markerSize.foreach(
      x => attrList = Attribute(null, "marker-size", x.toString, attrList))
    Option(markerFillColor).foreach(
      x => attrList = Attribute(null, "fill", formatColor(x), attrList))
    Option(strokeColor).foreach(
      x => attrList = Attribute(null, "stroke", formatColor(x), attrList)
    )
    strokeWidth.foreach(
      x => attrList = Attribute(null, "stroke-width", x.toString, attrList)
    )
    attrList
  }
}

class PaintOptionsAPI[T](config: PaintOptions, update: PaintOptions => T) {
  @WebMethod(action = "Marker symbol")
  def marker(x: Marker) = update(config.copy(marker = x))

  @WebMethod(action = "Marker width in pixels")
  def markerSize(x: Int) = update(config.copy(markerSize = Some(x * x)))

  @WebMethod(action = "Marker fill color")
  def markerFillColor(x: Color) = update(config.copy(markerFillColor = x))

  @WebMethod(action = "Color of line")
  def strokeColor(x: Color) = update(config.copy(strokeColor = x))

  @WebMethod(action = "Stroke width in pixelsl")
  def strokeWidth(x: Int) = update(config.copy(strokeWidth = Some(x)))

}

trait AxisType extends EnumTrait

object AxisType {

  case object linear extends AxisType

  case object log extends AxisType

}

trait Marker extends EnumTrait

object Marker {

  case object circle extends Marker

  case object cross extends Marker

  case object diamond extends Marker

  case object square extends Marker

  case object `triangle-down` extends Marker

  case object `triangle-up` extends Marker

}