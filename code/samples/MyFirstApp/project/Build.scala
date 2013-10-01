import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "MyFirstApp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "commons-io" % "commons-io" % "2.3"	
  )
  
val hello = TaskKey[Unit]("hello", "Prints 'Hello World'")

  val helloTask = hello := {
    println("Hello World")
	//org.mef.framework.Logger.debug("abc");
  }  

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here     
	helloTask,
	resolvers += Resolver.file("meflib.jar", file("lib")) transactional()	
  )

}
