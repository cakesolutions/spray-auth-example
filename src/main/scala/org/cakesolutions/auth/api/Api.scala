package org.cakesolutions.auth.api

import akka.actor._
import spray._
import routing._
import http.{StatusCodes, HttpResponse}
import akka.util.Timeout

class Api extends Actor
  with RouteConcatenation
  with UsersAuthenticationDirectives
  with ExampleService {
  
  def actorRefFactory = context.system

  val routes = exampleRoutes

  def receive = runRoute(routes)

}
