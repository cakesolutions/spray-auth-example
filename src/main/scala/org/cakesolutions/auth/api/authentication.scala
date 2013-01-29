package org.cakesolutions.auth.api

import spray.routing.{
  HttpService, RequestContext,
  AuthenticationFailedRejection,
  AuthenticationRequiredRejection
}
import concurrent.Future
import spray.routing.authentication.Authentication
import org.cakesolutions.auth.domain._
import org.cakesolutions.auth.core.control._

/** We can use the following functions to enable/disable access to
  * one or more route, using Spray's ``authentication`` function.
  *
  * {{{
  * ... with AuthenticationDirectives {
  *   ...
  *   path("secure" / "endpoint") {
  *     authenticate(userHandlerYouWrite) { auth =>
  *     }
  *   }
  *   ...
  * }
  * }}}
  */
trait AuthenticationDirectives {
  this: HttpService =>

  val authController: AuthController

  //We wrap this into a future to make it non blocking. After all, we
  //could access the DB here and don't want this to be our application
  //bottleneck.
  def doAuthenticate(password: String): Future[Option[(Auth, Role)]]

  /* We can insert here any kind of logic. 
   *
   * The return type is something you will could use inside your spray
   * routes. For the purpose of this example, we'll return only the
   * Auth, but we could choose to return also the role, if we need it,
   * but this information is implicit in the user case class and can be
   * retrieved with a DB lookup.
   */
  def adminOnly: RequestContext => Future[Authentication[Auth]] = {
    ctx: RequestContext =>
      val token = getToken(ctx)
      if (token.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "cake")))
      else doAuthenticate(token.get).map {
        auth =>
          if (auth.isDefined && auth.get._2.id == 0)
            Right(auth.get._1)
          else
            Left(AuthenticationFailedRejection("cake"))
      }
  }

  /* Notice the repetition, we are simply copying the code from the
   * admin handler, changing only a single condition! This is no good!
   * Can we do better! Sure we can! (see below).
   */
  def customerOnly: RequestContext => Future[Authentication[Auth]] = {
    ctx: RequestContext =>
      val token = getToken(ctx)
      if (token.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "cake")))
      else doAuthenticate(token.get).map {
        auth =>
          if (auth.isDefined && auth.get._2.id == 2)
            Right(auth.get._1)
          else
            Left(AuthenticationFailedRejection("cake"))
      }
  }

  /* This is a generic handler taking the role as parameter.
   * Now we can throw away the other twos!
   */
  def withRole(role: Int): RequestContext => Future[Authentication[Auth]] = {
    ctx: RequestContext =>
      val token = getToken(ctx)
      if (token.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "cake")))
      else doAuthenticate(token.get).map {
        auth =>
          if (auth.isDefined && auth.get._2.id == role)
            Right(auth.get._1)
          else
            Left(AuthenticationFailedRejection("cake"))
      }
  }

  /* Extract the token from an HTTP Header or URL. */
  def getToken(ctx: RequestContext): Option[AuthToken] = {
    //This is needed in case the auth_token is passed as a GET parameter.
    //It's up to you to remove this part of code or not.
    val query = ctx.request.queryParams.get("auth_token")
    if (query.isDefined)
      Some(query.get)
    else {
      val header = ctx.request.headers.find(_.name == "x-auth-token")
      header.map { _.value }
    }
  }
}

trait UsersAuthenticationDirectives
  extends AuthenticationDirectives {
  this: HttpService =>

  val authController = new AuthController

  override def doAuthenticate(token: AuthToken) = {
    Future {authController.doAuth(token)}
  }
}
