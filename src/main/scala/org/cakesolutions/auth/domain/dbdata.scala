package org.cakesolutions.auth.domain

/** A possible solution here is to have inside this class
 * all the domain data you want to persist, or scatter it
 * across the whole domain package, it's up to you.
 * Type synonym can be defined inside domain/package.scala
 */

case class User(id: UserId, role: RoleId)

/** For this example, we'll use a straightforward strategy.
 * We'll create three different user roles:
 * 1) Role(0, "admin")
 * 2) Role(1, "manager")
 * 3) Role(2, "customer")
 */
case class Role(id: RoleId, description: String)

case class Auth(userId: UserId, token: AuthToken)
