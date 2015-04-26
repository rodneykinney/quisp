package quisp

import spray.json._
import DefaultJsonProtocol._

import scala.language.implicitConversions

trait Point {
  def X: Option[Double]

  def Y: Option[Double]

  def Name: Option[String]
}

case class XYValue(x: Double, y: Double) extends Point {
  def X = Some(x)

  def Y = Some(y)

  def Name = None
}

case class YValue(value: Double) extends Point {
  def X = None

  def Y = Some(value)

  def Name = None
}

case class NamedXYValue(x: Option[Double], y: Option[Double], name: Option[String]) extends Point {
  def X = x

  def Y = y

  def Name = name
}



trait SeriesData {
  def points: Seq[Point]
}

trait SeriesDataConversions {

  implicit def toDoubleIterable[T <% Double](
    x: Iterable[T]): Iterable[Double]
  = x.map(_.toDouble)

  implicit def toIterableTupleDouble[T1 <% Double, T2 <% Double](
    x: Iterable[(T1, T2)]): Iterable[(Double, Double)]
  = x.map { case (a, b) => (a.toDouble, b.toDouble)}

  implicit def toTupleIterableDouble[T1 <% Double, T2 <% Double](
    x: (Iterable[T1], Iterable[T2])): (Iterable[Double], Iterable[Double])
  = (x._1.map(_.toDouble), x._2.map(_.toDouble))

  implicit def toIterableTupleStringDouble[T <% Double](
    x: Iterable[(String, T)]): Iterable[(String, Double)]
  = x.map { case (l, y) => (l, y.toDouble)}

  implicit def toIterableTupleDoubleString[T <% Double](
    x: Iterable[(T, String)]): Iterable[(Double, String)]
  = x.map { case (y, l) => (y.toDouble, l)}

  implicit def toIterableDoubleFunction[T <% Iterable[Double]](
    x: (T, Double => Double)): (Iterable[Double], Double => Double) = (x._1, x._2)

  implicit class DataFromStringAndIterable[T <% Iterable[Double], S <% Iterable[String]](
    xy: (T, S))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (l, y) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromArrayAndString[T <% Double](
    xy: (Array[T], Iterable[String]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (l, y) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromArrayAndStringArray[T <% Double](
    xy: (Array[T], Array[String]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (l, y) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromStringAndArray[T <% Double](
    xy: (Iterable[String], Array[T]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (y, l) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromStringArrayAndArray[T <% Double](
    xy: (Array[String], Array[T]))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (y, l) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromIterableAndString[T <% Iterable[Double], S <% Iterable[String]](
    xy: (S, T))
    extends SeriesData {
    def points = xy._2.zip(xy._1).map {
      case (y, l) => NamedXYValue(y = Some(y.toDouble), x = None, name = Some(l))
    }.toSeq
  }

  implicit class DataFromIterableStringDouble[T <% Iterable[(String, Double)]](ly: T)
    extends SeriesData {
    def points = ly.map {
      case (l, y) => NamedXYValue(name = Some(l), y = Some(y.toDouble), x = None)
    }.toSeq
  }

  implicit class DataFromArrayStringDouble[T <% Double](ly: Array[(String, T)])
    extends SeriesData {
    def points = ly.map {
      case (l, y) => NamedXYValue(name = Some(l), y = Some(y.toDouble), x = None)
    }.toSeq
  }

  implicit class DataFromIterableDoubleString[T <% Iterable[(Double, String)]](ly: T)
    extends SeriesData {
    def points = ly.map {
      case (y, l) => NamedXYValue(name = Some(l), y = Some(y.toDouble), x = None)
    }.toSeq
  }

  implicit class DataFromIterableFunction[T <% Iterable[Double]](
    xy: (T, Double => Double))
    extends SeriesData {
    def points = xy._1.map(x => XYValue(x, xy._2(x))).toSeq
  }

  implicit class DataFromArrayFunction[T <% Double](xy: (Array[T], Double => Double))
    extends SeriesData {
    def points = xy._1.map(x => XYValue(x, xy._2(x))).toSeq
  }

  implicit class DataFromFunctionIterable[T <% Iterable[Double]](xy: (Double => Double, T))
    extends SeriesData {
    def points = xy._2.map(x => XYValue(x.toDouble, xy._1(x))).toSeq
  }

  implicit class DataFromFunctionArray[T <% Double](xy: (Double => Double, Array[T]))
    extends SeriesData {
    def points = xy._2.map(x => XYValue(x.toDouble, xy._1(x))).toSeq
  }

  implicit class DataFromPairOfIterables[T <% (Iterable[Double], Iterable[Double])](xy: T)
    extends SeriesData {
    def points = xy._1.zip(xy._2).map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromPairOfArrays[T1 <% Double, T2 <% Double](xy: (Array[T1], Array[T2]))
    extends SeriesData {
    def points = xy._1.zip(xy._2).map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromIterableTuple[T <% Iterable[(Double, Double)]](xy: T)
    extends SeriesData {
    def points = xy.map { case (x, y) => XYValue(x.toDouble, y.toDouble)}.toSeq
  }

  implicit class DataFromIterable[T <% Iterable[Double]](y: T)
    extends SeriesData {
    def points = y.map(y => YValue(y.toDouble)).toSeq
  }

  implicit class DataFromStringIterable[S <% Iterable[String]](l: S)
    extends SeriesData {
    def points = l.zipWithIndex.map {
      case (n, i) => NamedXYValue(x = None, y = None, name = Some(n))
    }
      .toSeq
  }

}