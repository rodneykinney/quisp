package quisp.flot

import quisp._

import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/26/15.
 */
case class FlotRootConfig(
  series: Seq[Series],
  width: Int = 400,
  height: Int = 400
  ) {

}

trait FlotRootAPI[T <: UpdatableChart[T, FlotRootConfig]]
  extends UpdatableChart[T, FlotRootConfig] with API {
  @WebMethod(action = "Chart dimensions")
  def size(width: Int, height: Int) = update(config.copy(width = width, height = height))
}


class FlotGenericAPI(
  var config: FlotRootConfig,
  val display: ChartDisplay[ConfigurableChart[FlotRootConfig], Int])
  extends FlotRootAPI[FlotGenericAPI]


case class Series(
  data: Seq[Point]
  )
