import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "AppTwo"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "commons-io" % "commons-io" % "2.3"	
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
	resolvers += Resolver.file("meflib.jar", file("lib")) transactional()	
  )

}
