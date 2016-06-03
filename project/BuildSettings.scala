import sbt._
import Keys._

object BuildSettings {

  // Basic settings for our app
  lazy val basicSettings = Seq[Setting[_]](
    organization  := "com.github.gbeaty",
    version       := "0.2",
    description   := "A Scala client library for the Amazon Product Advertising API",
    scalaVersion  := "2.11.8",
    scalacOptions := Seq("-deprecation", "-encoding", "utf8"))

  lazy val scalapacSettings = basicSettings
}
