scalaVersion := "2.11.5"

name := "Quisp"

version := "0.0.5"

libraryDependencies ++= Seq(
	"io.spray" %%  "spray-json" % "1.3.1",
	"net.databinder" %% "unfiltered-filter" % "0.8.3",
	"net.databinder" %% "unfiltered-jetty" % "0.8.3"
	)
