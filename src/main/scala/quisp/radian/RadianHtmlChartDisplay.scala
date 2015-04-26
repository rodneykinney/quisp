package quisp.radian

import quisp.HtmlChartDisplay

import scala.xml.NodeSeq

/**
 * Created by rodneykinney on 4/22/15.
 */
class RadianHtmlChartDisplay extends HtmlChartDisplay[RadianRootConfig] {
  protected var resourceURL =
    "https://cdn.rawgit.com/rodneykinney/quisp/radian/resources/radian"

  override def metaTag = {
    <meta charset="utf-8">
      <title>Quisp</title>
      <link rel="stylesheet" href={s"$resourceURL/css/bootstrap.min.css"}></link>
      <link rel="stylesheet" href={s"$resourceURL/css/radian.css"}></link>
      <script src={s"$resourceURL/js/jquery.js"}></script>
      <script src={s"$resourceURL/js/jquery.csv.js"}></script>
      <script src={s"$resourceURL/js/bootstrap.min.js"}></script>
      <script src={s"$resourceURL/js/escodegen.browser.js"}></script>
      <script src={s"$resourceURL/js/estraverse.js"}></script>
      <script src={s"$resourceURL/js/d3.v2.js"}></script>
      <script src={s"$resourceURL/js/angular.min.js"}></script>
      <script src={s"$resourceURL/js/radian.min.js"}></script>
      <script>
        angular.module('Quisp', ['radian']).
        controller('QuispCtrl', [function()
        {{
        }}]);
      </script>

    </meta>
  }
  override def renderChart(config: RadianRootConfig) = config.html

  override def html(content: NodeSeq) =
  <html ng-app="Quisp">
    {content}
  </html>


  override def body(content: NodeSeq) =
  <body ng-controller="QuispCtrl">
    {content}
  </body>
}
