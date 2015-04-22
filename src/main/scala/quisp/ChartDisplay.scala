package quisp

import unfiltered.util.Port
import quisp.server.ChartServer

import scala.util.{Failure, Try}

/**
 * Created by rodneykinney on 4/14/15.
 */
trait ChartDisplay[T, TRef] {
  def addChart(chart: T): TRef

  def updateChart(id: TRef, newChart: T): TRef

  def removeChart(id: TRef): TRef

  def charts: Seq[T]
}

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

abstract class HtmlChartDisplay[TConfig] extends UndoableChartDisplay[TConfig] {
  def renderChartsToHtml(): String

  private var chartServer: Option[ChartServer] = None
  private var port = Port.any

  private def url = s"http://${java.net.InetAddress.getLocalHost.getCanonicalHostName}:${port}"

  private var serverEnabled = true

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
    Try {
      java.awt.Desktop.getDesktop.browse(new java.net.URI(link))
      link
    }
        .orElse(Try(s"open $link" !!))
        .orElse(Try(s"xdg-open $link" !!))
  }

  /**
   * Launches the server which hosts the charts. InetAddress.getLocalHost requires a properly configured /etc/hosts
   * on linux machines.
   * Assigns a random port
   */
  def startServer() {
    if (!serverRunning) {
      serverEnabled = true
      chartServer = Some(new ChartServer(port))
      refresh()
      openWindow(url) match {
        case Failure(msg) =>
          println(s"Error opening browser: $msg")
        case _ => ()
      }
      println(s"Server running at $url")
    }
  }

  def stopServer() {
    chartServer.map(_.stop)
    chartServer = None
    serverEnabled = false
  }

  def refresh(): Unit = {
    if (serverEnabled)
      startServer()

    val contentWithPlaceholder = renderChartsToHtml()
    val contentHash = contentWithPlaceholder.hashCode.toHexString
    val actualContent = contentWithPlaceholder.replaceAllLiterally("HASH_PLACEHOLDER", contentHash)

    chartServer.map(_.refresh(actualContent, contentHash))
  }

  val refreshScript =
    <script type="text/javascript">
      var contentHash = 'HASH_PLACEHOLDER';
      $.ajax( {{url: '/check',
      data:
      {{'clientContentHash ':[contentHash]}}
      ,
      success: function (result)
      {{location.reload(); }}
      }})
    </script>
}

