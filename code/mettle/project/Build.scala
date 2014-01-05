import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "mettle"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    cache,
    "commons-io" % "commons-io" % "2.3",
    "commons-lang" % "commons-lang" % "2.6"
    
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here 
	resolvers += Resolver.file("sfxlib.jar", file("lib")) transactional(),	         
	resolvers += Resolver.file("ST-4.0.7.jar", file("lib")) transactional()	         
  )

}
