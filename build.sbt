scalaVersion := "2.11.5"

name := "Quisp"

organization := "com.github.rodneykinney"

version := "0.6.0"

libraryDependencies ++= Seq(
	"io.spray" %%  "spray-json" % "1.3.1",
	"net.databinder" %% "unfiltered-filter" % "0.8.3",
	"net.databinder" %% "unfiltered-jetty" % "0.8.3"
	)

// publishSigned
// sonatypeRelease

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := (
  <url>https://github.com/rodneykinney/quisp</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:rodneykinney/quisp</url>
    <connection>scm:git:git@github.com:rodneykinney/quisp.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rodneykinney</id>
      <name>Rodney Kinney</name>
      <url>https://github.com/rodneykinney</url>
    </developer>
  </developers>)
