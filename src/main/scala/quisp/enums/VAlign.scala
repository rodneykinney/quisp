package quisp.enums

/**
 * @author rodneykinney
 */
object VAlign {
  val TOP = top
  val MIDDLE = middle
  val BOTTOM = bottom

  case object top extends VAlign

  case object middle extends VAlign

  case object bottom extends VAlign

}

sealed trait VAlign extends EnumTrait