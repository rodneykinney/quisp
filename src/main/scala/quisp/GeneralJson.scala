package quisp

import spray.json.DefaultJsonProtocol._
import spray.json._

import java.awt.Color

/**
 * General-purpose JSON formats
 * @author rodneykinney
 */
object GeneralJson {
  import scala.language.implicitConversions
  implicit def writerToFormat[T](writer: JsonWriter[T]) = new JsonFormat[T] {

    override def write(obj: T): JsValue = writer.write(obj)

    override def read(json: JsValue): T = ???
  }

  implicit val pointJS: JsonFormat[Point] = new JsonWriter[Point] {
    def write(p: Point) = (p.X, p.Y, p.Z, p.Name) match {
      case (Some(x), Some(y), None, Some(s)) => (x, y, s).toJson
      case (None, Some(y), None, Some(s)) => (s, y).toJson
      case (Some(x), None, None, Some(s)) => (x, s).toJson
      case (None, None, None, Some(s)) => s.toJson

      case (Some(x), Some(y), None, None) => (x, y).toJson
      case (Some(x), None, None, None) => x.toJson
      case (None, Some(y), None, None) => y.toJson

      case (None, None, None, None) => "".toJson

      case (Some(x), Some(y), Some(z), None) => (x,y,z).toJson

      case _ => sys.error("Invalid data series format")
    }
  }

  implicit val colorJS: JsonFormat[Color] =
    new JsonWriter[Color] {
      def write(c: Color) = c.getAlpha match {
        case 255 => "#%02x%02x%02x".format(c.getRed, c.getGreen, c.getBlue).toJson
        case a => s"rgba(${c.getRed},${c.getGreen},${c.getBlue},${a.toDouble / 255})".toJson
      }
    }


}
