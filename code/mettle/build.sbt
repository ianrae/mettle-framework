name := "mettle"

version := "0.5.0-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  javaCore, javaJdbc, javaEbean,  cache,
  cache,
    "commons-io" % "commons-io" % "2.3",
    "commons-lang" % "commons-lang" % "2.6",
	"play-sprig" % "play-sprig_2.11" % "0.2-SNAPSHOT",
	"twixt" % "twixt_2.11" % "0.1.0-SNAPSHOT"	
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

resolvers += "release repository" at  "http://hakandilek.github.com/maven-repo/releases/"

resolvers += "snapshot repository" at "http://hakandilek.github.com/maven-repo/snapshots/"

//for heroku, uncomment next line and do 
// play eclipse clean compile
//resolvers += Resolver.url("Mettle Repository", url("http://ianrae.github.io/snapshot/"))(Resolver.ivyStylePatterns)


