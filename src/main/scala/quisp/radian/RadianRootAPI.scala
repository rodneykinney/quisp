package quisp.radian

import quisp.highcharts.{Orientation, Point, EnumTrait}
import quisp.{UpdatableChart, ChartDisplay, API, ConfigurableChart}

import scala.xml.transform.{RuleTransformer, RewriteRule}
import scala.xml._

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/22/15.
 */
case class RadianRootConfig(
    series: Seq[SeriesConfig],
    title: String = "",
    width: Int = 400,
    height: Int = 400,
    xAxis: AxisConfig = AxisConfig("x"),
    yAxis: AxisConfig = AxisConfig("y"),
    lineOptions: LineOptions = LineOptions(),
    markerOptions: MarkerOptions = MarkerOptions(),
    legend: LegendConfig = LegendConfig()
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
          lineOptions.addAttributes _ andThen {
        case e: Elem => series.foldLeft(e) { case (elem, s) => s.insertChildren(elem) }
      } andThen
          legend.insertChildren _
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

  @WebMethod(action = "Data series attributes")
  def series(idx: Int) = config.series(idx).api(s => update(config.copy(series = config.series.updated(idx, s))))

  @WebMethod(action = "Legend options")
  def legend = config.legend.api(l => update(config.copy(legend = l)))
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
    range.foreach { case (min, max) => attList = Attribute(null, s"range-$id", s"$min,$max", attList) }
    Option(axisType).foreach(
      t => attList = Attribute(null, s"axis-$id-transform", t.toString, attList))
    attList
  }
}

trait ColorFormat {
  def formatColor(c: Color) = c.getAlpha match {
    case 255 => "#%02x%02x%02x".format(c.getRed, c.getGreen, c.getBlue)
    case a => s"rgba(${c.getRed},${c.getGreen},${c.getBlue},${a.toDouble / 255})"
  }
}

trait AddElementAttributes {
  def addAttributes(e: Elem): Elem =
    new RuleTransformer(rewrite(e)).transform(e).head.asInstanceOf[Elem]

  def rewrite(target: Elem) = new RewriteRule {
    override def transform(n: Node) = n match {
      case e: Elem if e == target => e % attributes
      case _ => n
    }
  }

  def attributes: MetaData

}

case class SeriesConfig(points: Seq[Point],
    seriesType: SeriesType,
    lineOptions: Option[LineOptions] = None,
    markerOptions: Option[MarkerOptions] = None) extends InsertChildren {
  def api[T](update: SeriesConfig => T) = new SeriesAPI(this, update)

  def childElements = {
    val id = s"data_${this.hashCode.toHexString}"
    val e: Seq[Elem] = (seriesType, lineOptions, markerOptions) match {
      case (SeriesType.line, _, Some(m)) if m.enabled =>
        val lines = lineOptions.map(x => (x.addAttributes _)).getOrElse(identity[Elem] _)(<lines x={s"[[$id.x]]"} y={s"[[$id.y]]"}></lines>)
        val points = m.addAttributes(<points x={s"[[$id.x]]"} y={s"[[$id.y]]"}></points>)
        List(lines, points)
      case (SeriesType.scatter, Some(l), _) if l.enabled =>
        val lines = l.addAttributes(<lines x={s"[[$id.x]]"} y={s"[[$id.y]]"}></lines>)
        val points = markerOptions.map(x => (x.addAttributes _)).getOrElse(identity[Elem] _)(<points x={s"[[$id.x]]"} y={s"[[$id.y]]"}></points>)
        List(lines, points)
      case (SeriesType.line, _, _) =>
        List(<lines x={s"[[$id.x]]"} y={s"[[$id.y]]"}></lines>)
      case (SeriesType.scatter, _, _) =>
        List(<points x={s"[[$id.x]]"} y={s"[[$id.y]]"}></points>)
    }
    val data = <plot-data name={s"$id"} format="csv" cols="x,y">
      {points.zipWithIndex.map {
        case (p, i) => s"${p.X.getOrElse(i)},${p.Y.getOrElse(i)}"
      }.mkString("\n")}
    </plot-data>
    Seq(data) ++ e
  }
}

class SeriesAPI[T](config: SeriesConfig, update: SeriesConfig => T) extends API {
  @WebMethod(action = "Paint options for line plot")
  def lineOptions = config.lineOptions.getOrElse(LineOptions()).api(o => update(config.copy(lineOptions = Some(o))))
  @WebMethod(action = "Paint options for point plot")
  def markerOptions = config.markerOptions.getOrElse(MarkerOptions()).api(o => update(config.copy(markerOptions = Some(o))))
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
    enabled: Boolean = true,
    strokeColor: Color = null,
    strokeWidth: Option[Int] = None
    ) extends AddElementAttributes with ColorFormat {
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

case class LegendConfig(
    enabled: Boolean = true,
    position: Option[(Int, Int)] = None,
    borderThickness: Option[Int] = None,
    borderColor: Color = null,
    orientation: Orientation = null,
    horizontalMargin: Option[Int] = None,
    verticalMargin: Option[Int] = None,
    labelPosition: Order = null

    ) extends InsertChildren with ColorFormat {
  def api[T](update: LegendConfig => T) = new LegendAPI(this, update)
  def childElements = {
    if (enabled) {
      val e = <legend></legend>
      var attList: MetaData = Null
      position.foreach {
        case (x, y) => attList = Attribute(null, "position", s"$x,$y", attList)
      }
      borderThickness.foreach {
        x => attList = Attribute(null, "frame-thickness", s"$x", attList)
      }
      Option(borderColor).foreach {
        x => attList = Attribute(null, "frame-color", formatColor(x), attList)
      }
      Option(orientation).foreach {
        x => attList = Attribute(null, "orientation", x.toString, attList)
      }
      horizontalMargin.foreach {
        x => attList = Attribute(null, "horizontal-margin", s"$x", attList)
      }
      verticalMargin.foreach {
        x => attList = Attribute(null, "vertical-margin", s"$x", attList)
      }
      Option(labelPosition).foreach {
        x => attList = Attribute(null, "label-position", x.toString, attList)
      }
      List(e.copy(attributes = attList))
    }
    else {
      List()
    }
  }

}

class LegendAPI[T](config: LegendConfig, update: LegendConfig => T) extends API {
  @WebMethod(action = "Display legend")
  def enabled(x: Boolean) = update(config.copy(enabled = x))
  @WebMethod(action = "X,Y position of legend")
  def position(x: Int, y: Int) = update(config.copy(position = Some(x, y)))
  @WebMethod(action = "Thickness of legend border")
  def borderThickness(x: Int) = update(config.copy(borderThickness = Some(x)))
  @WebMethod(action = "Color of legend border")
  def bordercolor(x: Color) = update(config.copy(borderColor = x))
  @WebMethod(action = "Orientation (vertical/horizontal)")
  def orientation(x: Orientation) = update(config.copy(orientation = x))
  @WebMethod(action = "Horizontal space between legend entries")
  def horizontalMargin(x: Int) = update(config.copy(horizontalMargin = Some(x)))
  @WebMethod(action = "Vertical space between legend entries")
  def verticalMargin(x: Int) = update(config.copy(verticalMargin = Some(x)))
  @WebMethod(action = "Position of labels relative to symbols (before/after)")
  def labelPosition(x: Order) = update(config.copy(labelPosition = x))
}

case class MarkerOptions(
    enabled: Boolean = true,
    marker: Symbol = null,
    markerSize: Option[Int] = None,
    markerFillColor: Color = null
    ) extends AddElementAttributes with ColorFormat {
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
  @WebMethod(action = "Draw line between points")
  def enabled(x: Boolean) = update(config.copy(enabled = x))

  @WebMethod(action = "Color of line")
  def strokeColor(x: Color) = update(config.copy(strokeColor = x))

  @WebMethod(action = "Stroke width in pixelsl")
  def strokeWidth(x: Int) = update(config.copy(strokeWidth = Some(x)))

}

class MarkerOptionsAPI[T](config: MarkerOptions, update: MarkerOptions => T) {
  @WebMethod(action = "Draw markers on points")
  def enabled(x: Boolean) = update(config.copy(enabled = x))

  @WebMethod(action = "Marker symbol")
  def symbol(x: Symbol) = update(config.copy(marker = x))

  @WebMethod(action = "Marker width in pixels")
  def markerSize(x: Int) = update(config.copy(markerSize = Some(x * x)))

  @WebMethod(action = "Marker fill color")
  def markerFillColor(x: Color) = update(config.copy(markerFillColor = x))

}

sealed trait AxisType extends EnumTrait

object AxisType {

  case object linear extends AxisType

  case object log extends AxisType

}

sealed trait Symbol extends EnumTrait

object Symbol {

  case object circle extends Symbol

  case object cross extends Symbol

  case object diamond extends Symbol

  case object square extends Symbol

  case object `triangle-down` extends Symbol

  case object `triangle-up` extends Symbol

}

sealed trait SeriesType extends EnumTrait

object SeriesType {
  val AREA = area
  val BAR = bar
  val LINE = line
  val PIE = pie
  val SCATTER = scatter
  case object area extends SeriesType
  case object bar extends SeriesType
  case object line extends SeriesType
  case object pie extends SeriesType
  case object scatter extends SeriesType
}

sealed trait Order extends EnumTrait
object Order {
  case object before extends Order
  case object after extends Order
}
