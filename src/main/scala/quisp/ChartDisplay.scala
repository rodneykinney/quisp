package quisp

import quisp.server.ChartServer
import unfiltered.util.Port

import scala.util.{Failure, Try}
import scala.xml.{Elem, NodeBuffer, NodeSeq}

/**
 * A container to which charts can be added, removed, and updated
 * @tparam T the type of the chart data
 * @tparam TRef the type of an ID that identifies a chart
 * @author rodneykinney
 */
trait ChartDisplay[T, TRef] {
  def addChart(chart: T): TRef

  def updateChart(id: TRef, newChart: T): TRef

  def removeChart(id: TRef): TRef

  def charts: Seq[T]
}

/** A chart container that supports undo/redo */
trait UndoableChartDisplay[TConfig] extends ChartDisplay[ConfigurableChart[TConfig], Int] {
  def refresh(): Unit

  private var chartVector = Vector.empty[Option[(ConfigurableChart[TConfig], TConfig)]]
  private var commandHistory = Vector.empty[Command]
  private var lastCommand = -1

  def charts = chartVector.flatten.map(_._1)

  def chartConfigs = chartVector.flatten.map(_._2)

  def addChart(chart: ConfigurableChart[TConfig]) = {
    val idx = chartVector.size
    executeCommand(Add(idx, chart, chart.config))
    refresh()
    idx
  }

  def updateChart(idx: Int, newChart: ConfigurableChart[TConfig]) = {
    if (idx >= 0 && idx < chartVector.size) {
      val (chart, config) = chartVector(idx).get
      executeCommand(Update(idx, newChart, config, newChart.config))
      refresh()
    }
    idx
  }

  def removeChart(idx: Int) = {
    if (idx >= 0 && idx < chartVector.size) {
      chartVector(idx) match {
        case Some((chart, config)) =>
          executeCommand(Remove(idx, chart, config))
          refresh()
        case None => ()
      }
    }
    idx
  }

  def undo() = {
    if (lastCommand >= 0) {
      commandHistory(lastCommand).undo.execute
      lastCommand -= 1
      refresh()
    }
    else {
      println("Nothing to undo")
    }
  }

  def redo() = {
    if (lastCommand < commandHistory.size - 1) {
      commandHistory(lastCommand + 1).execute
      lastCommand += 1
      refresh()
    }
    else {
      println("Nothing to redo")
    }
  }

  def clear() =
    for ((Some(p), i) <- chartVector.zipWithIndex) {
      removeChart(i)
    }

  def executeCommand(cmd: Command) = {
    commandHistory = commandHistory.take(lastCommand + 1) :+ cmd
    lastCommand += 1
    cmd.execute
  }

  trait Command {
    def execute: Unit

    def undo: Command
  }

  case class Add(idx: Int, chart: ConfigurableChart[TConfig], config: TConfig) extends Command {
    def execute = {
      if (idx == chartVector.size)
        chartVector = chartVector :+ Some((chart, config))
      else if (idx < chartVector.size)
        chartVector = chartVector.updated(idx, Some((chart, config)))
    }

    def undo = Remove(idx, chart, config)
  }

  case class Remove(idx: Int, chart: ConfigurableChart[TConfig], config: TConfig) extends Command {
    def execute = {
      chartVector = chartVector.updated(idx, None)
    }

    def undo = Add(idx, chart, config)
  }

  case class Update(idx: Int, chart: ConfigurableChart[TConfig], oldConfig: TConfig, newConfig: TConfig) extends Command {
    def execute = {
      chart.config = newConfig
      chartVector = chartVector.updated(idx, Some((chart, newConfig)))
    }

    def undo = Update(idx, chart, newConfig, oldConfig)
  }


}

/** A chart container that renders charts by publishing HTML to an embedded server */
abstract class HtmlChartDisplay[TConfig] extends UndoableChartDisplay[TConfig] {
  private var chartServer: Option[ChartServer] = None
  private var port = Port.any
  private def url = s"http://localhost:${port}"
  private var serverEnabled = true
  private var browserEnabled = true

  def serverRunning = chartServer.isDefined

  def serverPort(port: Int): Unit = {
    this.port = port
    if (serverRunning) {
      stopServer
      startServer()
    }
  }

  private def openWindow(link: String) = {
    import scala.sys.process._
    import scala.language.postfixOps
    Try {
      java.awt.Desktop.getDesktop.browse(new java.net.URI(link))
      link
    }
      .orElse(Try(s"open $link" !!))
      .orElse(Try(s"xdg-open $link" !!))
  }

  /** Start embedded web server to serve HTML */
  def startServer() {
    if (!serverRunning) {
      serverEnabled = true
      chartServer = Some(new ChartServer(port))
      refresh()
      if (browserEnabled) {
        openWindow(url) match {
          case Failure(msg) =>
            println(s"Error opening browser: $msg")
          case _ => ()
        }
      }
      println(s"Server running at $url")
    }
  }

  /** Stop embedded web server */
  def stopServer() {
    chartServer.map(_.stop)
    chartServer = None
    serverEnabled = false
  }

  /**
   * If true (default) launch a browser window pointing to embedded server.
   * Can be set to false if only remote access is desired.
   */
  def enableBrowser(enabled: Boolean): Unit = {
    browserEnabled = enabled
  }

  def refresh(): Unit = {
    if (serverEnabled)
      startServer()

    val contentWithPlaceholder = renderChartsToHtml()
    val contentDigest = contentWithPlaceholder.hashCode.toHexString
    val actualContent = contentWithPlaceholder.replaceAllLiterally("DIGEST_PLACEHOLDER", contentDigest)

    chartServer.map(_.refresh(actualContent, contentDigest))
  }

  private var nColumns = 1

  def columns(n: Int) = {
    nColumns = n
    refresh()
  }

  def metaTag: Elem

  def renderChart(config: TConfig): NodeSeq

  def renderChartsToHtml(): String = {

    "<!doctype html>" +
      html {
        val buf = new NodeBuffer
        buf.append(
          <head>
            <title>Quisp</title>{metaTag}{refreshScript}
          </head>)
        buf.append(body(renderCharts))
        buf
      }
  }

  protected def html(content: NodeSeq) =
    <html>
      {content}
    </html>

  protected def body(content: NodeSeq) =
    <body>
      {content}
    </body>

  protected def renderCharts: NodeSeq = chartTable

  // This script is included in the generated page URL
  // Initiates a client request to a /check path on the embedded server
  // This endpoint will block until there is fresh content on the server
  // When unblocked, the client will refresh the entire page to load the new content
  protected val refreshScript =
    <script type="text/javascript">
      var contentDigest = 'DIGEST_PLACEHOLDER';
      $.ajax( {{url: '/check',
      data:
      {{'clientContentDigest ':[contentDigest]}}
      ,
      success: function (result)
      {{location.reload(); }}
      }})
    </script>

  protected def chartTable = {
    <table>
      {if (chartConfigs.size > 0) {
      val rowHtml = for (chartRow <- chartConfigs.sliding(nColumns, nColumns)) yield {
        chartRow.map(c => <td>
          {renderChart(c)}
        </td>)
      }
      rowHtml.map(r => <tr>
        {r}
      </tr>)
    }
    else {
      <tr></tr>
    }}
    </table>
  }
}

