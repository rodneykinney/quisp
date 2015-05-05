package quisp.enums

/**
 * @author rodneykinney
 */
object HcSymbol {
  val CIRCLE = circle
  val SQUARE = square
  val DIAMOND = diamond
  val TRIANGLE = triangle
  val TRIANGLE_DOWN = `triangle-down`

  case object circle extends HcSymbol

  case object square extends HcSymbol

  case object diamond extends HcSymbol

  case object triangle extends HcSymbol

  case object `triangle-down` extends HcSymbol

}

sealed trait HcSymbol extends EnumTrait