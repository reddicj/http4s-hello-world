package com.example.helloworld

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
// import io.circe.{Decoder, Encoder}
// import org.http4s._
// import org.http4s.circe._
import zio.interop.catz._
import zio.RIO
import zio.clock.Clock

object HelloWorldService {

  type HttpTask[A] = RIO[Clock, A]

  val routes: HttpRoutes[HttpTask] = {
    
    val dsl = new Http4sDsl[HttpTask]{}
    import dsl._

    // implicit def circeJsonDecoder[A: Decoder]: EntityDecoder[HttpTask, A] = jsonOf[HttpTask, A]
    // implicit def circeJsonEncoder[A: Encoder]: EntityEncoder[HttpTask, A] = jsonEncoderOf[HttpTask, A]

    HttpRoutes.of[HttpTask] {
      
      case GET -> Root / "hello" / name => {
        val msg: String = s"Hello $name"
        Ok(AppLogger.info(msg).as(msg).provideLayer(AppLogger.layer))
      }

      case GET -> Root / "error" => {
        val msg: String = "Boom!"
        InternalServerError(AppLogger.error(msg).as(msg).provideLayer(AppLogger.layer))
      }
    }
  }
}