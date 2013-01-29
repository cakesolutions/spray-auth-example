package org.cakesolutions.auth.api

import spray.routing._
import concurrent.Future

trait ExampleService extends HttpService
  with AuthenticationDirectives {

  /* Read more about Spray routing here:
   * http://spray.io/documentation/spray-routing/
   * 
   * In this example we'll create three routes.
   * 1) A "clean" route, everyone will have access at it.
   * 2) A "customer only" route, only customer will access it.
   * 3) A "admin only" route, only the admin will access it.
   *
   * For simplicity, we'll pass the token as get parameter instead
   * that as HTTP Header param. This way, we can test our routes in
   * the browser directly.
   */

  val exampleRoutes = {

      //This doesn't need authentication, because we want it to
      //be a "grants all". In theory, if we still want to bring the
      //auth in scope (see below), we can write another trivial handler
      //which "grants all".
      //
      //Suppose you want to do something different for different roles:
      //you can create an authentication handler which simply brings in
      //scope the auth object, and then inside the complete put some
      //conditional code or whatever. It should also possible to use some
      //clever spray directive to avoid the nasty conditional branch, but
      //we are not aware of anything like that.
      path("resource" / "all") {
        get {
          complete {
            "Morning, guest."
          }
        }
      } ~
      path("resource" / "admin-only") {

        //If we succeed with the authentication, we
        //bring in scope as a variable the result of
        //the adminOnly handler, an Auth object. We can
        //now pass this object around.
        authenticate(adminOnly) { auth =>
          get {
            complete {
              "Morning, " + auth.userId
            }
          }
        }
      } ~
      path("resource" / "customer-only") {
        authenticate(customerOnly) { auth =>
          get {
            complete {
              "Morning, " + auth.userId
            }
          }
        }
      } ~
      path("resource" / "better-admin") {
        authenticate(withRole(0)) { auth =>
          get {
            complete {
              "Morning, " + auth.userId
            }
          }
        }
      } ~
      path("resource" / "better-customer") {
        authenticate(withRole(2)) { auth =>
          get {
            complete {
              "Morning, " + auth.userId
            }
          }
        }
      }

    }
}
