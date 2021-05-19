package com.example.helloworld

import org.http4s._
import zio._
import zio.interop.catz._
import zio.test.Assertion._
import zio.test._

object HttpTestUtils {

  def checkRequest[R, A](
    program: RIO[R, Response[RIO[R, *]]],
    expectedStatus: Status,
    expectedBody: Option[A]
  )(implicit ev: EntityDecoder[RIO[R, *], A]): RIO[R, TestResult] = for {
    actual <- program
    bodyResult <- expectedBody.fold[RIO[R, TestResult]](assertM(actual.bodyText.compile.toVector)(isEmpty))(expected => assertM(actual.as[A])(equalTo(expected)))
    statusResult = assert(actual.status)(equalTo(expectedStatus))
  } yield bodyResult && statusResult
}