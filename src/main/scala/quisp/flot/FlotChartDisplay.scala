package quisp.flot

import quisp.HtmlChartDisplay

import scala.xml.{Elem, NodeSeq}

/**
 * Generates HTML to render charts via Flot library
 * @author rodneykinney
 */
class FlotChartDisplay extends HtmlChartDisplay[Chart] {

  val resourceRoot = "https://cdn.rawgit.com/rodneykinney/quisp/v0.6.0/resources/flot"

  override def metaTag: Elem = {
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.min.js"}></script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.min.js"}></script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.symbol.min.js"}>
      </script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.categories.min.js"}>
      </script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.axislabels.js"}>
      </script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.stack.min.js"}>
      </script>
      <script language="javascript" type="text/javascript"
              src={s"$resourceRoot/js/jquery.flot.pie.min.js"}>
      </script>
    </meta>
  }

  override def renderChart(config: Chart): NodeSeq =
    config.html
}
