package quisp.highcharts

import quisp.{ConfigurableChart, HtmlChartDisplay}

import scala.util.Random
import spray.json._
import HighchartsJson._

/**
 * Created by rodneykinney on 4/16/15.
 */
class HighchartsHtmlDisplay extends HtmlChartDisplay[RootConfig] {

  private var nColumns = 1

  def columns(n: Int) = {
    nColumns = n
    refresh()
  }

  def renderChartsToHtml(): String = {

    """<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">""" +
        <html>
          <head>
            <title>Quisp</title>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

              <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
              <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts.js"></script>
              <script type="text/javascript" src="http://code.highcharts.com/4.0.4/modules/exporting.js"></script>
              <script type="text/javascript" src="http://code.highcharts.com/4.0.4/highcharts-more.js"></script>

              {refreshScript}

            </meta>
          </head>
          <body>
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
          </body>
        </html>
  }

  def renderChart(hc: RootConfig) = {
    val json = scala.xml.Unparsed(hc.toJson.toString)
    val x = <a>
      {json}
    </a>.toString
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
