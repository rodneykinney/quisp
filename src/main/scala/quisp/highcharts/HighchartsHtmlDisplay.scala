package quisp.highcharts

import quisp.{ConfigurableChart, HtmlChartDisplay}

import scala.util.Random
import spray.json._
import HighchartsJson._

/**
 * Created by rodneykinney on 4/16/15.
 */
class HighchartsHtmlDisplay extends HtmlChartDisplay[HcRootConfig] {

  override def metaTag = {
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

      <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/modules/exporting.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts-more.js"></script>

    </meta>
  }

  override def renderChart(hc: HcRootConfig) = {
    val json = scala.xml.Unparsed(hc.toJson.toString)
    val containerId = json.hashCode.toHexString
    <div id={s"container$containerId"}></div>
        <script type="text/javascript">
          $ (function()
          {{$(
          {s"'#container$containerId'"}
          ).highcharts(
          {json}
          );}}
          );
        </script>
  }

}
