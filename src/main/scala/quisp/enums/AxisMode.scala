package quisp.enums

/**
 * @author rodneykinney
 */
object AxisMode {
  val CATEGORIES = categories

  case object categories extends AxisMode

}

sealed trait AxisMode extends EnumTrait