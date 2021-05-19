package com.example.helloworld

import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.Router
import zio.interop.catz._
import zio.clock.Clock
import zio.{ExitCode => ZExitCode, _}

// Useful Refs:
// https://http4s.org/v0.21/dsl/
// https://github.com/mschuwalow/zio-todo-backend/blob/develop/src/main/scala/com/schuwalow/todo/Main.scala
// https://medium.com/@wiemzin/zio-with-http4s-and-doobie-952fba51d089

object Main extends App {

  type HttpTask[A] = RIO[Clock, A]

  val httpApp: HttpApp[HttpTask] = 
    Router[HttpTask]("/" -> HelloWorldService.routes).orNotFound

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ZExitCode] = (for {
    _ <- AppLogger.info("Starting HelloWorld service")
    _ <- ZIO.sleep(java.time.Duration.ofSeconds(5))
    _ <- HttpAppRunner.run(httpApp, 8080)
  } yield ZExitCode.success)
    .mapError(_ => ZExitCode.failure)
    .merge
    .provideCustomLayer(AppLogger.layer)
}
