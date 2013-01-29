package org.cakesolutions.auth.main

import akka.actor._
import spray.can.server._
import org.cakesolutions.auth.api.Api

object Main extends App
  with SprayCanHttpServerApp {

  val service = system.actorOf(Props[Api], "spray-auth-example")
  newHttpServer(service) ! Bind("0.0.0.0", 8080)

}
