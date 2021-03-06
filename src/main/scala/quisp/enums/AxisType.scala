package quisp.enums

/**
 * @author rodneykinney
 */
object AxisType {
  val CATEGORY = category
  val DATETIME = datetime
  val LINEAR = linear
  val LOGARITHMIC = logarithmic

  case object category extends AxisType

  case object datetime extends AxisType

  case object linear extends AxisType

  case object logarithmic extends AxisType

}

sealed trait AxisType extends EnumTrait