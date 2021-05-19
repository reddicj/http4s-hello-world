package com.example.helloworld

import zio.test._
import org.http4s._
import org.http4s.implicits._
import zio.interop.catz._
import Main.HttpTask

object HelloWorldServiceTest extends DefaultRunnableSpec {

  def run(request: Request[HttpTask]): HttpTask[Response[HttpTask]] = 
    Main.httpApp.run(request)
  
  def spec = suite("Hello World tests")(

    testM("Test hello endpoint") {

      HttpTestUtils.checkRequest(
        run(Request(Method.GET, uri"/hello/foo")),
        Status.Ok,
        Some("Hello foo")
      )
    },
    testM("Test error endpoint") {

      HttpTestUtils.checkRequest(
        run(Request(Method.GET, uri"/error")),
        Status.InternalServerError,
        Option.empty[String]
      )
    }
  )
}