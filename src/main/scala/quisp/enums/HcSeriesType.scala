package quisp.enums

/**
 * @author rodneykinney
 */
object HcSeriesType {
  val AREA = area
  val AREASPLINE = areaspline
  val BAR = bar
  val BOXPLOT = boxplot
  val COLUMN = column
  val LINE = line
  val PIE = pie
  val SCATTER = scatter
  val SPLINE = spline

  case object area extends HcSeriesType

  case object areaspline extends HcSeriesType

  case object bar extends HcSeriesType

  case object boxplot extends HcSeriesType

  case object column extends HcSeriesType

  case object line extends HcSeriesType

  case object pie extends HcSeriesType

  case object scatter extends HcSeriesType

  case object spline extends HcSeriesType

}

sealed trait HcSeriesType extends EnumTrait