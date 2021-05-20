package com.example.helloworld

import com.typesafe.config.ConfigFactory
import zio._
import AppLogger.Logger

final case class AppConfig(podName: Option[String]) {

  val isLeader: Boolean = podName.fold(true)(_.endsWith("-0"))

  lazy val print: String = List[Option[String]](
    podName.map(n => s"podName = $n"),
    Some(s"isLeader = $isLeader")
  ).flatten.mkString(", ")
}

object AppConfig {

  val fromConfig: RIO[Logger, AppConfig] = (for {
    config <- ZIO.effect(ConfigFactory.load())
    read <- ZIO.effect(config.getString("k8s.podname").trim)
    podname = if (read == "None") None else Some(read)
  } yield AppConfig(podname))
    .tapError(AppLogger.error("Error reading config", _))
    .tap(config => AppLogger.info(s"AppConfig: [${config.print}]"))
}