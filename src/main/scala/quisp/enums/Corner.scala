package quisp.enums

/**
 * Created by rodneykinney on 4/28/15.
 */
object Corner {
  val NE = ne
  val NW = nw
  val SE = se
  val SW = sw

  case object ne extends Corner

  case object nw extends Corner

  case object se extends Corner

  case object sw extends Corner

}

sealed trait Corner extends EnumTrait