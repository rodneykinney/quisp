package quisp.radian

import quisp.ConfigurableChart

/**
 * Created by rodneykinney on 4/22/15.
 */
trait RootAPI extends ConfigurableChart[RadianRootConfig]{
  var config: RadianRootConfig
}

case class RadianRootConfig(title: String = null)
