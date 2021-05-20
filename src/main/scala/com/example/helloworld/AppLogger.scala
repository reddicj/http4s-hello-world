package com.example.helloworld

import zio._
import com.typesafe.scalalogging.StrictLogging

trait AppLogger {
  def info(msg: String): UIO[Unit]
  def error(msg: String): UIO[Unit]
  def error(msg: String, cause: Throwable): UIO[Unit]
}

object AppLogger extends StrictLogging {

  type Logger = Has[AppLogger]

  private val live: AppLogger = new AppLogger {
    def info(msg: String): UIO[Unit] = ZIO.effectTotal(logger.info(msg))
    def error(msg: String): UIO[Unit] = ZIO.effectTotal(logger.error(msg))
    def error(msg: String, cause: Throwable): UIO[Unit] = ZIO.effectTotal(logger.error(msg, cause))
  }

  val layer: ULayer[Logger] = ZLayer.succeed(live)

  def info(msg: String): URIO[Logger, Unit] = ZIO.service[AppLogger].flatMap(_.info(msg))
  def error(msg: String): URIO[Logger, Unit] = ZIO.service[AppLogger].flatMap(_.error(msg))
  def error(msg: String, cause: Throwable): URIO[Logger, Unit] = ZIO.service[AppLogger].flatMap(_.error(msg, cause))
}