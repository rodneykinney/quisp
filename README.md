# Quisp -- QUick Interactive Scala Plots


Quisp is a DSL for creating and manipulating interactive charts that render in a browser window,
allowing you to visualize data interactively in the Scala REPL.

## Sample Session

From the Scala REPL:

    import quisp.Plot._

    // Bring up browser window displaying simple line chart
    val chart = line(1 to 100)

    // Display online help
    chart.help

    // Add title to chart.  Browser will refresh automatically
    chart.title.text("A Linear Relationship")

    // Add a second chart to the page
    val chart2 = line(1 to 100, (t: Double) => math.sin(t * .04 * math.Pi))

    // Remove first chart
    chart.remove()

    // Set the y axis limits
    chart2.yAxis.range(-1,1)

    // Display help for y axis settings
    chart2.yAxis.help

    // Set the x axis type
    chart2.xAxis.axisType(AxisType.logarithmic)

    // Add a second series to the chart
    chart2.addSeries(1 to 100, (t: Double) => math.cos(t * .02 * math.Pi))

    // Name the series
    chart2.series(0).name("Sin")
    chart2.series(1).name("Cos")


See the [Wiki](https://github.com/rodneykinney/quisp/wiki) for more examples

## Server Mode
By default, Quisp will launch a browser window to display charts.  In server mode, the following
commands are available:

 - undo() -- Undo last change
 - redo() -- Redo last undo
 - columns(nColumns) - Set number of columns in browser
 - refresh() -- Refresh display
 - clear() -- Remove all charts

## Offline Mode
Offline mode can be entered by calling `stopServer()`, in which case no content will be shown in
the browser.

    import quisp.Plot._

    // Enter offline mode
    stopServer()

    val chart = line(1 to 10)

    // Get HTML page as string
    val html = renderChartsToHtml()

    // Get JSON object corresponding to chart
    import spray.json._
    import DefaultJsonProtocol._
    import quisp.highcharts.HighchartsJson._
    chart.config.toJson

Server Mode can be entered again by calling `startServer()`.

## License
Quisp generates JSON compatible with [Highcharts](http://www.highcharts.com/) to render the
plots.  Quisp is released under the Apache 2.0 license.  Highcharts is a commercial
product, although free for non-profits and personal projects.



