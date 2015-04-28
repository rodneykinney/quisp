package quisp

/**
 * Created by rodneykinney on 4/28/15.
 */
package object enums {
  implicit val symbolJS = EnumTrait.jsFormat[FlotSymbol]
  implicit val cornerJS = EnumTrait.jsFormat[Corner]
  implicit val hAlignJS = EnumTrait.jsFormat[HAlign]
  implicit val axisModeJS = EnumTrait.jsFormat[AxisMode]
  implicit val vAlignJS = EnumTrait.jsFormat[VAlign]
  implicit val axisTypeJS = EnumTrait.jsFormat[AxisType]
  implicit val orientationJS = EnumTrait.jsFormat[Orientation]
  implicit val stackingJS = EnumTrait.jsFormat[Stacking]
  implicit val dashStyleJS = EnumTrait.jsFormat[DashStyle]
  implicit val markerSymbolJS = EnumTrait.jsFormat[HcSymbol]
  implicit val seriesTypeJS = EnumTrait.jsFormat[HcSeriesType]
}
