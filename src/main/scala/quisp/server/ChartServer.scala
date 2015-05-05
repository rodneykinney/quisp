package quisp.server

import unfiltered.request._
import unfiltered.response._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

/**
 * Embedded server that serves HTML to render charts
 * @author rodneykinney
 */
class ChartServer(port: Int) {
  val httpServer = unfiltered.jetty.Server.http(port).plan(new WebApp)
  httpServer.start()
  stopServerOnMainThreadExit

  private var p = Promise[Unit]()
  private var content = "Initializing..."
  private var contentDigest = ""

  /** Notify clients that content has changed */
  def refresh(newContent: String,
    newContentDigest: String) = {
    content = newContent
    contentDigest = newContentDigest
    p.success()
    p = Promise[Unit]()
  }

  private def stopServerOnMainThreadExit: Unit = {
    val mainThread = Thread.currentThread()
    implicit val ctx = scala.concurrent.ExecutionContext.global
    Future {
      mainThread.join()
      Thread.sleep(5000)
      httpServer.stop()
    }

  }

  private class WebApp extends unfiltered.filter.Plan {
    def intent = {
      // The actual HTML content
      case req@GET(Path(Seg(Nil)) & Params(params)) =>
        implicit val responder = req
        HtmlContent ~> ResponseString(content)
      // Content freshness probe.
      // Client will send digest of content it currently has loaded
      // If this digest matches the digest of the current content, block
      // until content is refreshed.
      // If the digest does not match, return immediately to allow the client to load new content
      case req@GET(Path(Seg("check" :: Nil)) & Params(params)) =>
        val clientContentDigest = params.values.headOption.map(_.headOption).flatten
        implicit val responder = req
        val response = s""""$contentDigest""""
        // If content on the server side is the same as loaded by the client, block
        // Block will be released on calling refresh()
        if (clientContentDigest.forall(_ == contentDigest)) {
          try {
            Await.result(p.future, Duration.Inf)
          }
          catch {
            case ex: InterruptedException => () // Shutdown
          }
        }
        JsonContent ~> ResponseString(response)
      case _ => Pass
    }
  }

  def stop(): Unit = {
    httpServer.stop
    httpServer.destroy
  }
}