import sbt._
import Keys._

object PrestasacBuild extends Build {

  import Dependencies._
  import BuildSettings._

  // Configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  // Define our project, with basic project information and library dependencies
  lazy val prestasacProject = Project("prestasac", file("."))
    .settings(scalapacSettings: _*)
    .settings(
      resolvers ++= resolutionRepos,
      libraryDependencies ++= Seq(
        Libraries.codec,
        Libraries.xml,
        Libraries.json4sJackson))
}
