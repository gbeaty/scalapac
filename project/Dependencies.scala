import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    ScalaToolsSnapshots,
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype OSS Resources" at "https://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases"
  )

  object V {
    val commons     = "1.6"
  }

  object Libraries {
    // Used for request signing
    val codec       = "commons-codec" % "commons-codec"  % V.commons
    val xml         = "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.3"
    val json4sJackson = "org.json4s" % "json4s-jackson_2.11" % "3.2.10"
  }
}
