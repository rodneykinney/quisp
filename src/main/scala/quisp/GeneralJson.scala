package quisp

import spray.json._
import DefaultJsonProtocol._

import java.awt.Color

/**
 * Created by rodneykinney on 4/26/15.
 */
object GeneralJson {
  implicit def writerToFormat[T](writer: JsonWriter[T]) = new JsonFormat[T] {

    import quisp.EstensibleJsFormat._

    override def write(obj: T): JsValue = writer.write(obj)

    override def read(json: JsValue): T = ???
  }

  implicit val colorJS: JsonFormat[Color] =
    new JsonWriter[Color] {
      def write(c: Color) = c.getAlpha match {
        case 255 => "#%02x%02x%02x".format(c.getRed, c.getGreen, c.getBlue).toJson
        case a => s"rgba(${c.getRed},${c.getGreen},${c.getBlue},${a.toDouble / 255})".toJson
      }
    }
  implicit val pointJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(obj: Point) = obj match {
      case p: XYValue => (p.x, p.y).toJson
      case p: YValue => p.value.toJson
      case p: NamedXYValue => (p.x, p.y, p.name).toJson
    }
  }


}
