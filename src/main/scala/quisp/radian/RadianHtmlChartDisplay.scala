package quisp.radian

import quisp.HtmlChartDisplay

/**
 * Created by rodneykinney on 4/22/15.
 */
class RadianHtmlChartDisplay extends HtmlChartDisplay[RadianRootConfig] {
  var resourceURL =
    "https://cdn.rawgit.com/rodneykinney/quisp/radian/resources/radian"
  override def renderChartsToHtml(): String = {
    "<!doctype html>" +
        <html lang="en" ng-app="Quisp">
          <head>
            <meta charset="utf-8">
              <title>Quisp</title>
              <link rel="stylesheet" href={s"$resourceURL/css/bootstrap.min.css"}></link>
              <link rel="stylesheet" href={s"$resourceURL/css/radian.css"}></link>

              {refreshScript}

            </meta>
          </head>
          <body ng-controller="MyCtrl">

            <div class="container">
              <h4>Example 1</h4>
              <plot>
                <lines x="[[seq(0,2*PI,101)]]" y="[[sin(x)]]"></lines>
              </plot>
            </div>

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
              controller('MyCtrl', [function()
              {{
              }}]);
            </script>
          </body>
        </html>.toString
  }
}
