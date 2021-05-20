val ZIOVersion = "1.0.7"
val Http4sVersion = "0.21.16"
val CirceVersion = "0.13.0"
val LogbackVersion = "1.2.3"

organization := "com.example"
name := "hello-world-server"
version := "1.0.3"
scalaVersion := "2.13.5"
  
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-test" % ZIOVersion,
  "dev.zio" %% "zio-test-sbt" % ZIOVersion,
  "dev.zio" %% "zio-interop-cats" % "2.4.1.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  "com.typesafe" % "config" % "1.4.1"
)
  
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

enablePlugins(JavaAppPackaging, DockerPlugin)
mainClass in Compile := Some("com.example.helloworld.Main")
dockerBaseImage := "docker.imanage.com/ravn-java11-base:latest"
dockerRepository := Some("docker.imanage.com")
