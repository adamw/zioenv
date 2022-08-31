package zioenv

import zio.*

trait UserRegistration:
  def register(u: User): Task[User]

object UserRegistration:
  case class UserRegistrationImpl(userRepo: UserRepo, userNotifier: UserNotifier) extends UserRegistration:
    override def register(u: User): Task[User] =
      for
        _ <- userRepo.insert(u)
        _ <- userNotifier.notify(u, "Welcome!")
      yield u

  val layer: ZLayer[UserRepo with UserNotifier, Nothing, UserRegistration] = ZLayer {
    for
      userRepo <- ZIO.service[UserRepo]
      userNotifier <- ZIO.service[UserNotifier]
    yield UserRegistrationImpl(userRepo, userNotifier)
  }

  def register(u: User): ZIO[UserRegistration, Throwable, User] =
    ZIO.serviceWithZIO(_.register(u))
