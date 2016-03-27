
scalaVersion := "2.11.8"

organization := "com.github.rssh"
name := "trackedfuture-example"

libraryDependencies += "com.github.rssh" %% "trackedfuture" % "0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"


fork := true
javaOptions += "-javaagent:../agent/target/scala-2.11/trackedfuture_2.11-0.1.jar"
