package quisp.radian

import quisp.{UpdatableChart, ChartDisplay, API, ConfigurableChart}

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/22/15.
 */
case class RadianRootConfig(
  title: String = "",
  width: Int = 400,
  height: Int = 400
  ) {
  def html = {
    <plot
    width={s"$width"}
    height={s"$height"}>
      <lines x="[[seq(0,2*PI,101)]]" y="[[sin(x)]]"></lines>
    </plot>
  }
}

trait RadianRootAPI[T <: UpdatableChart[T, RadianRootConfig]]
  extends UpdatableChart[T, RadianRootConfig] with API {
  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))

  @WebMethod(action = "Chart title")
  def title(x: String) = update(config.copy(title = x))
}

class RadianGenericAPI(
  var config: RadianRootConfig,
  val display: ChartDisplay[ConfigurableChart[RadianRootConfig], Int])
  extends RadianRootAPI[RadianGenericAPI]
