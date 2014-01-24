name := "app4"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mettle" % "mettle_2.10" % "0.2-SNAPSHOT" 
)     

play.Project.playJavaSettings

//resolvers ++= Seq(
//  Resolver.url("Mettle Repository", new URL("http://ianrae.github.io/snapshot/"))(Resolver.ivyStylePatterns)
//)

