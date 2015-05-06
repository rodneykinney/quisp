package quisp.enums

/**
 * @author rodneykinney
 */
object HAlign {
  val LEFT = left
  val CENTER = center
  val RIGHT = right

  case object left extends HAlign

  case object center extends HAlign

  case object right extends HAlign

}

sealed trait HAlign extends EnumTrait