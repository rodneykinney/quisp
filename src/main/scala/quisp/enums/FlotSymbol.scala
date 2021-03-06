package quisp.enums

/**
 * @author rodneykinney
 */
object FlotSymbol {
  val CIRCLE = circle
  val SQUARE = square
  val TRIANGLE = triangle
  val DIAMOND = diamond
  val CROSS = cross

  case object circle extends FlotSymbol

  case object square extends FlotSymbol

  case object triangle extends FlotSymbol

  case object diamond extends FlotSymbol

  case object cross extends FlotSymbol

}

sealed trait FlotSymbol extends EnumTrait