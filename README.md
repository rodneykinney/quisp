# Quisp -- QUick Interactive Scala Plots

Quisp is a DSL for creating and manipulating interactive charts that render in a browser window,
allowing you to visualize data interactively in the Scala REPL.

## To use Quisp

Add to your `build.sbt`:

    resolvers ++= Seq(
      "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
    )
    
    scalaVersion := "2.11.5" // Quisp is only available for Scala 2.11 presently

    libraryDependencies ++= Seq(
	  "com.github.rodneykinney" %%  "quisp" % "0.5.0"
    )
    
## Sample Session

From the Scala REPL (`sbt console`):

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
    chart2.xAxis.logarithmic(true)

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

    // Get HTML for individual chart
    chart.config.html

Server Mode can be entered again by calling `startServer()`.

## Plotting implementations

Importing `quisp.Plot._` will use [Flot](http://www.flotcharts.org/) to render charts.  Flot is released under the MIT license.  

Importing `quisp.highcharts.Plot._` will use [Highcharts](http://www.highcharts.com/)
to produce charts with more interactivity than Flot charts.  Highcharts is a commercial
product, although free for non-profits and personal projects.




