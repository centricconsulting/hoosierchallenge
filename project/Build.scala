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
    "commons-io" % "commons-io" % "2.4",
    "org.drools" % "drools-core" % "5.5.0.Final",
    "org.drools" % "drools-compiler" % "5.5.0.Final",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "commons-codec" % "commons-codec" % "1.8",
    "org.apache.httpcomponents" % "httpclient" % "4.3"
  )
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
      //Add Custom Repository
      resolvers += "Rhinofly Internal Release Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local"
  )

}
