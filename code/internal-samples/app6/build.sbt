name := "app6"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
   "mettle" % "mettle_2.10" % "0.2-SNAPSHOT" 
)     

play.Project.playJavaSettings
