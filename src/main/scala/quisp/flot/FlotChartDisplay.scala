package quisp.flot

import quisp.HtmlChartDisplay

import scala.xml.{Elem, NodeSeq}

/**
 * Created by rodneykinney on 4/26/15.
 */
class FlotChartDisplay extends HtmlChartDisplay[FlotChart] {

  val resourceRoot = "https://rawgit.com/rodneykinney/quisp/flot/resources/flot"

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

  override def renderChart(config: FlotChart): NodeSeq =
    config.html
}
