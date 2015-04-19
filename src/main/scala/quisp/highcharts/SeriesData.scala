package quisp.highcharts

import scala.language.implicitConversions

trait SeriesData {
  def points: Seq[Point]

  def categories =
    if (points.forall(_.isInstanceOf[RichPoint]))
      Some(points.map(_.Name.get))
    else
      None
}

trait SeriesDataConversions {

  implicit class DataFromStringAndIterable[T <% Double](xy: (Iterable[T], Iterable[String]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map { case (l, y) => RichPoint(y = Some(y.toDouble), x = None, name = l)}.toSeq
  }

  implicit class DataFromStringAndArray[B <% Double](xy: (Iterable[B], Array[String]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map { case (l, y) => RichPoint(y = Some(y.toDouble), x = None, name = l)}.toSeq
  }

  implicit class DataFromIterableStringTuple[T <% Double](ly: (Iterable[(String, T)]))
    extends SeriesData {
    def points = ly.map { case (l, y) => RichPoint(name = l, y = Some(y.toDouble), x = None)}.toSeq
  }

  implicit class DataFromArrayStringTuple[T <% Double](ly: (Array[(String, T)]))
    extends SeriesData {
    def points = ly.map { case (l, y) => RichPoint(name = l, y = Some(y.toDouble), x = None)}.toSeq
  }

  implicit class DataFromIterableFunction[T1 <% Double, T2 <% Double](xy: (Iterable[T1], T1 => T2))
    extends SeriesData {
    def points = xy._1.map(x => XYValue(x.toDouble, xy._2(x).toDouble)).toSeq
  }

  implicit class DataFromArrayFunction[T1 <% Double, T2 <% Double](xy: (Array[T1], T1 => T2))
    extends SeriesData {
    def points = xy._1.map(x => XYValue(x.toDouble, xy._2(x).toDouble)).toSeq
  }

  implicit class DataFromFunctionArray[T1 <% Double, T2 <% Double](xy: (T1 => T2, Array[T1]))
    extends SeriesData {
    def points = xy._2.map(x => XYValue(x.toDouble, xy._1(x).toDouble)).toSeq
  }

  implicit class DataFromFunctionIterable[T1 <% Double, T2 <% Double](xy: (T1 => T2, Iterable[T1]))
    extends SeriesData {
    def points = xy._2.map(x => XYValue(x.toDouble, xy._1(x).toDouble)).toSeq
  }

  implicit class DataFromPairOfIterables[T1 <% Double, T2 <% Double](xy: (Iterable[T1], Iterable[T2]))
    extends SeriesData {
    def points = xy._1.zip(xy._2).map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromPairOfArrays[T1 <% Double, T2 <% Double](xy: (Array[T1], Array[T2]))
    extends SeriesData {
    def points = xy._1.zip(xy._2).map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromIterableTuple[T1 <% Double, T2 <% Double](xy: (Iterable[(T1, T2)]))
    extends SeriesData {
    def points = xy.map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromArrayTuple[T1 <% Double, T2 <% Double](xy: (Array[(T1, T2)]))
    extends SeriesData {
    def points = xy.map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromIterable[T <% Double](y: (Iterable[T]))
    extends SeriesData {
    def points = y.map(y => YValue(y.toDouble)).toSeq
  }

  implicit class DataFromArray[T <% Double](y: (Array[T]))
    extends SeriesData {
    def points = y.map(y => YValue(y.toDouble)).toSeq
  }

  implicit class DataFromStringIterable(l: Iterable[String])
    extends SeriesData {
    def points = l.zipWithIndex.map { case (n, i) => RichPoint(x = None, y = None, name = n)}.toSeq
  }

  implicit class DataFromStringArray(l: Array[String])
    extends SeriesData {
    def points = l.zipWithIndex.map { case (n, i) => RichPoint(x = None, y = None, name = n)}
  }


}