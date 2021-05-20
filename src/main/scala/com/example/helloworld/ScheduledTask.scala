package com.example.helloworld

import java.time.Duration

import zio._
import zio.clock.Clock
import AppLogger.Logger

object ScheduledTask {

  val schedule = Schedule.spaced(Duration.ofSeconds(5))

  def run(config: AppConfig): URIO[Clock with Logger, Unit] = if (config.isLeader) {
    AppLogger.info(s"Running scheduled task - podId = ${config.podName.getOrElse("None")}")
      .repeat(schedule)
      .unit
  } else ZIO.unit
}