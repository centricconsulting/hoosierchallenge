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
    "securesocial" %% "securesocial" % "master-SNAPSHOT"
  )


  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
      resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

}
