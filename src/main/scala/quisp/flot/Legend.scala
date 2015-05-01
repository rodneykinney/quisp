package quisp.flot

import quisp.enums.Corner
import quisp.{API, ExtensibleJsObject}
import spray.json.JsValue

import java.awt.Color
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/28/15.
 */
case class Legend(
  show: Option[Boolean] = None,
  labelBoxBorderColor: Color = null,
  noColumns: Option[Int] = None,
  position: Corner = null,
  backgroundColor: Color = null,
  additionalFields: Map[String, JsValue] = Map()
  ) extends ExtensibleJsObject {
  def api[T](update: Legend => T) = new LegendAPI(this, update)
}

class LegendAPI[T](config: Legend, update: Legend => T) extends API {
  @WebMethod(action = "Show/hide legend")
  def enabled(x: Boolean) = update(config.copy(show = Some(x)))

  @WebMethod
  def borderColor(x: Color) = update(config.copy(labelBoxBorderColor = x))

  @WebMethod(action = "Position of Legend")
  def position(x: Corner) = update(config.copy(position = x))

  def backgroundColor(x: Color) = update(config.copy(backgroundColor = x))

  @WebMethod(action = "Number of columns in legend")
  def columns(x: Int) = update(config.copy(noColumns = Some(x)))
}
