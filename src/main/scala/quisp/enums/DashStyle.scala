package quisp.enums

/**
 * @author rodneykinney
 */
object DashStyle {
  val SOLID = Solid
  val SHORT_DASH = ShortDash
  val SHORT_DOT = ShortDot
  val SHORT_DASH_DOT = ShortDashDot
  val SHORT_DASH_DOT_DOT = ShortDashDotDot
  val DOT = Dot
  val DASH = Dash
  val LONG_DASH = LongDash
  val LONG_DASH_DOT_DOT = LongDashDotDot
  val LONG_DASH_DOT = LongDashDot
  val DASH_DOT = DashDot

  case object Solid extends DashStyle

  case object ShortDash extends DashStyle

  case object ShortDot extends DashStyle

  case object ShortDashDot extends DashStyle

  case object ShortDashDotDot extends DashStyle

  case object Dot extends DashStyle

  case object Dash extends DashStyle

  case object LongDash extends DashStyle

  case object LongDashDot extends DashStyle

  case object LongDashDotDot extends DashStyle

  case object DashDot extends DashStyle

}

sealed trait DashStyle extends EnumTrait