package quisp

import scala.collection.mutable.ListBuffer

import java.lang.reflect.Method
import javax.jws.WebMethod

/**
 * Created by rodneykinney on 4/22/15.
 */
trait ConfigurableChart[T] {
  var config: T
}

trait UpdatableChart[T <: UpdatableChart[T, TConfig], TConfig]
  extends ConfigurableChart[TConfig] {
  val display: ChartDisplay[ConfigurableChart[TConfig], Int]

  val index = display.addChart(this)

  @WebMethod(action = "Remove this chart from view")
  def remove() = display.removeChart(index)

  def update(newData: TConfig): T = {
    config = newData
    display.updateChart(index, this)
    this.asInstanceOf[T]
  }
}

trait API {
  def help =
    methodDescriptions.foreach(println)

  protected def methodDescriptions = {
    val methods = new ListBuffer[Method]
    var c: Class[_] = this.getClass
    while (c != null) {
      for {
        method <- c.getDeclaredMethods
        methodName = method.getName
        if method.getAnnotation(classOf[WebMethod]) != null
      }
        methods.append(method)
      c = c.getSuperclass
    }
    val msgs = for {
      m <- methods
    } yield {
      val msg = Option(m.getAnnotation(classOf[WebMethod])).
        map(_.action).filter(_.length > 0).map(s => s" -- $s").getOrElse("")
      val params = m.getParameterTypes.map(_.getSimpleName) match {
        case Array() => ""
        case a => a.mkString("(", ", ", ")")
      }
      s"${m.getName}$params$msg"
    }
    msgs.filterNot(_.startsWith("additionalField")).sorted ++
      msgs.filter(_.startsWith("additionalField"))
  }
}


