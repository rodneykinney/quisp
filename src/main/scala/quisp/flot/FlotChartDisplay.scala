package quisp.flot

import quisp.HtmlChartDisplay
import spray.json._
import DefaultJsonProtocol._

import scala.xml.{Unparsed, Elem, NodeSeq}

/**
 * Created by rodneykinney on 4/26/15.
 */
class FlotChartDisplay extends HtmlChartDisplay[FlotRootConfig] {

  val resourceRoot = "https://rawgit.com/rodneykinney/quisp/flot/resources/flot"

  override def metaTag: Elem = {
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.min.js"}></script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.min.js"}></script>
    </meta>
  }

  override def renderChart(config: FlotRootConfig): NodeSeq = {
    import FlotJson._
    val containerId = s"container_${config.hashCode.toHexString}"
    <div id ={containerId} style={s"width:${config.width}px;height:${config.height}px"}></div>
    <script type ="text/javascript">
      $ (function() {{
      $.plot({s"$containerId"},{Unparsed(config.series.toJson.toString)});
      }});
    </script>
  }
}
