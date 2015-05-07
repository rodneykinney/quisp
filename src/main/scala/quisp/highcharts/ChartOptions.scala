package quisp.highcharts

import quisp.{ExtensibleJsObjectAPI, ExtensibleJsObject}
import quisp.enums.HAlign
import spray.json.{JsValue, JsonWriter}

import java.awt.Color
import javax.jws.WebMethod

/**
 * Top-level chart options
 * @author rodneykinney
 */
case class ChartOptions(
  width: Int = 500,
  height: Int = 500,
  borderWidth: Int = 2,
  borderColor: Color = null,
  backgroundColor: Color = null,
  borderRadius: Option[Int] = None,
  spacing: Option[(Int, Int, Int, Int)] = None,
  plotBackgroundColor: Color = null,
  plotBorderColor: Color = null,
  plotBorderWidth: Option[Int] = None,
  plotShadow: Option[Boolean] = None,
  polar: Option[Boolean] = None,
  style: Map[String, String] = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: ChartOptions => T) = new ChartOptionsAPI(this, update)
}

class ChartOptionsAPI[T](chart: ChartOptions, update: ChartOptions => T) extends ExtensibleJsObjectAPI {
  @WebMethod(action="Overall width/height")
  def size(w: Int, h: Int) = update(chart.copy(width = w, height = h))

  @WebMethod(action="Color of border surrounding chart")
  def borderColor(x: Color) = update(chart.copy(borderColor = x))

  @WebMethod
  def backgroundColor(x: Color) = update(chart.copy(backgroundColor = x))

  @WebMethod(action = "Corner radius for chart border")
  def borderRadius(x: Int) = update(chart.copy(borderRadius = Some(x)))

  @WebMethod(action = "Pixel width of chart border")
  def borderWidth(x: Int) = update(chart.copy(borderWidth = x))

  @WebMethod(action = "Padding around outer edge: top, right, bottom, left")
  def spacing(top: Int, right: Int, bottom: Int, left: Int) = update(chart.copy(spacing = Some(
    (top, right, bottom, left))))

  @WebMethod(action = "Background color of the plot area")
  def plotBackgroundColor(x: Color) = update(chart.copy(plotBackgroundColor = x))

  @WebMethod(action = "Color of the plot area")
  def plotBorderColor(x: Color) = update(chart.copy(plotBorderColor = x))

  @WebMethod(action = "Pixel width of plot area border")
  def plotBorderWidth(x: Int) = update(chart.copy(plotBorderWidth = Some(x)))

  @WebMethod(action = "Show shadow (background color must be set)")
  def plotShadow(x: Boolean) = update(chart.copy(plotShadow = Some(x)))

  @WebMethod(action = "Use polar coordinates (r, theta)")
  def polar(x: Boolean) = update(chart.copy(polar = Some(x)))

  @WebMethod(action = "CSS style")
  def style(x: Map[String, String]) = update(chart.copy(style = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(chart.copy(additionalFields = chart.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))
}

case class ChartTitle(
  text: String = "",
  align: HAlign = HAlign.center,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: ChartTitle => T) = new ChartTitleAPI(this, update)
}

class ChartTitleAPI[T](ct: ChartTitle, update: ChartTitle => T) extends ExtensibleJsObjectAPI {
  @WebMethod
  def text(x: String) = update(ct.copy(text = x))

  @WebMethod(action = "Align.[left|right|center]")
  def align(x: HAlign) = update(ct.copy(align = x))

  @WebMethod(action = "Add additional values to the JSON object")
  def additionalField[V: JsonWriter](name: String, value: V)
  = update(ct.copy(additionalFields = ct.additionalFields + (name -> implicitly[JsonWriter[V]].write(value))))

}



