package com.example.helloworld

import cats.effect._
import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio.clock.Clock
import zio.{ZIO, RIO}
import zio.interop.catz._

object HttpAppRunner {

  def run[R <: Clock](httpApp: HttpApp[RIO[R, *]], port: Int): RIO[R, Unit] = 
    ZIO.runtime[R].flatMap { implicit rts =>
      type AppTask[A] = RIO[R, A]
      BlazeServerBuilder[AppTask](rts.platform.executor.asEC)
        .bindHttp(port, "0.0.0.0")
        .withHttpApp(CORS(httpApp))
        .serve
        .compile[AppTask, AppTask, ExitCode]
        .drain
    }
}