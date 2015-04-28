package quisp.enums

/**
 * Created by rodneykinney on 4/28/15.
 */
object Stacking {
  val NORMAL = normal
  val PERCENT = percent

  case object normal extends Stacking

  case object percent extends Stacking

}

sealed trait Stacking extends EnumTrait