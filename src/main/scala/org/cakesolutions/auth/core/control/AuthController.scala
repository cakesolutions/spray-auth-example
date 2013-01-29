package org.cakesolutions.auth.core.control

import org.cakesolutions.auth.domain._

/** This controller class is the responsible
 * of looking into the DB and retrieve information
 * about user. It can be whatever you want, but
 * limiting reponsibilities to just one class is
 * a good application of "separation of concerns".
 */
class AuthController {

  /** For the purpose of this demo, we won't talk
   * with the DB. Ideally, here you app should do
   * any kind of operations, including:
   * a) Caching
   * b) Fetching from LDAP
   * c) etc, etc
   *
   * The signature can be improved, but for the purpose of
   * this simple example, we'll send along also the role associated
   * with the user.
   */
  def doAuth(token: AuthToken): Option[(Auth, Role)] = {
    //Here, we are just creating and returning plain object. Insert
    //your DB and caching logic here.
    
    //Your real logic could be something like this: if you don't find
    //in the DB this token to be associated to any real user, return
    //None, otherwise, Auth info associated with that token.
    token match {
      case "customer" => Some((Auth("john", "customer"), Role(2, "customer")))
      case "admin" => Some((Auth("paul", "admin"), Role(0, "admin")))
      case _ => None
    }
  }

}
