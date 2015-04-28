package quisp.enums

/**
 * Created by rodneykinney on 4/28/15.
 */
object Orientation {
  val VERTICAL = vertical
  val HORIZONTAL = horizontal

  case object vertical extends Orientation

  case object horizontal extends Orientation

}

sealed trait Orientation extends EnumTrait