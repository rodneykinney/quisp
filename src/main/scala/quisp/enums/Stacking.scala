package quisp.enums

/**
 * @author rodneykinney
 */
object Stacking {
  val NORMAL = normal
  val PERCENT = percent

  case object normal extends Stacking

  case object percent extends Stacking

}

sealed trait Stacking extends EnumTrait