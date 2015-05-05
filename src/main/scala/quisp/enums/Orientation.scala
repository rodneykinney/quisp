package quisp.enums

/**
 * @author rodneykinney
 */
object Orientation {
  val VERTICAL = vertical
  val HORIZONTAL = horizontal

  case object vertical extends Orientation

  case object horizontal extends Orientation

}

sealed trait Orientation extends EnumTrait