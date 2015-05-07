package quisp.highcharts

import quisp.HtmlChartDisplay
import quisp.highcharts.HighchartsJson._
import spray.json._

/**
 * Render charts using Highcharts library
 * @author rodneykinney
 */
class HighchartsHtmlDisplay extends HtmlChartDisplay[Chart] {

  override def metaTag = {
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

      <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/modules/exporting.js"></script>
      <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts-more.js"></script>

    </meta>
  }

  override def renderChart(hc: Chart) = hc.html

}
