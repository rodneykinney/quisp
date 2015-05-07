package quisp

import spray.json.JsonWriter

import scala.collection.mutable.ListBuffer

import java.lang.reflect.Method
import javax.jws.WebMethod

/**
 * A chart object is added to a ChartDisplay.
 * It contains mutable state that is changed by ChartDisplay.update()
 * @author rodneykinney
 */
trait ConfigurableChart[T] {
  var config: T
}

/**
 * Initialized with a reference to a ChartDisplay.
 * This class adds itself to the display when instantiated
 * and updates the display when modified via the update() method
 * @tparam T The self-type of this chart
 * @tparam TConfig The type of the mutable config for this chart
 */
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

/**
 * An API instance is what is actually returned to the user
 * for manipulation in the REPL.
 * It will wrap a ConfigurableChart instance.
 * Methods on the API class will produce new configuration state
 * for the ConfigurableChart and call its update() method
 */
trait API {
  def help =
    methodDescriptions.foreach(println)

  // Any method with a @WebMethod annotation will appear in the help
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
    msgs
  }
}

trait ExtensibleJsObjectAPI extends API {
  override def methodDescriptions = {
    val m = super.methodDescriptions
    val special = "additionalField"
    m.filterNot(_.startsWith(special)) ++ m.filter(_.startsWith(special))
  }

  def additionalField[T: JsonWriter](name: String, value: T): Any
}


