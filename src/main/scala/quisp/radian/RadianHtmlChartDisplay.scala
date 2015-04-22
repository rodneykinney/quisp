package quisp.radian

import quisp.HtmlChartDisplay

/**
 * Created by rodneykinney on 4/22/15.
 */
class RadianHtmlChartDisplay extends HtmlChartDisplay[RadianRootConfig] {
  var resourceURL =
    "https://raw.githubusercontent.com/rodneykinney/quisp/radian/resources/radian"
  override def renderChartsToHtml(): String =
  s"""
     |<!doctype html>
     |<html lang="en" ng-app="Quisp">
     |<head>
     |  <meta charset="utf-8">
     |  <title>Radian Tutorial Examples: Section 1.1, Example 1</title>
     |  <link rel="stylesheet" href="$resourceURL/css/bootstrap.min.css"/>
     |  <link rel="stylesheet" href="$resourceURL/css/radian.css"/>
     |
     |  $refreshScript
     |
     |</head>
     |<body ng-controller="MyCtrl">
     |  <br>
     |  <br>
     |  <div class="container">
     |    <h4>Example 1</h4>
     |    <plot>
     |      <lines x="[[seq(0,2*PI,101)]]" y="[[sin(x)]]"></lines>
     |    </plot>
     |  </div>
     |
     |  <script src="$resourceURL/js/jquery.js"></script>
     |  <script src="$resourceURL/js/jquery.csv.js"></script>
     |  <script src="$resourceURL/js/bootstrap.min.js"></script>
     |  <script src="$resourceURL/js/escodegen.browser.js"></script>
     |  <script src="$resourceURL/js/estraverse.js"></script>
     |  <script src="$resourceURL/js/d3.v2.js"></script>
     |  <script src="$resourceURL/js/angular.min.js"></script>
     |  <script src="$resourceURL/js/radian.min.js"></script>
     |  <script>
     |angular.module('Quisp', ['radian']).
     |  controller('MyCtrl', [function()
     |{
     |}]);
     |  </script>
     |</body>
     |</html>
     |
   """.stripMargin
}
