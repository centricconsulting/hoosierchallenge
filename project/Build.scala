import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ccd-merge"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.drools" % "drools-core" % "5.5.0.Final"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
