import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "mettle"
  val appVersion      = "0.3.1-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    cache,
    "commons-io" % "commons-io" % "2.3",
    "commons-lang" % "commons-lang" % "2.6",
	"play-sprig" % "play-sprig_2.10" % "0.1-SNAPSHOT"	
    
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here 
	resolvers += Resolver.file("sfxlib.jar", file("lib")) transactional(),	         
	resolvers += Resolver.file("ST-4.0.7.jar", file("lib")) transactional(),	   
	resolvers += Resolver.url("play-sprig", new URL("http://ianrae.github.io/snapshot/"))(Resolver.ivyStylePatterns)
	
  )

}
