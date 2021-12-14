package zioenv

import zio.{Clock, ZIO}

object UserRegistration {
  def register(u: User): ZIO[Clock with DB, Throwable, User] = {
    for {
      _ <- UserModel.insert(u)
      _ <- UserNotifier.notify(u, "Welcome!")
    } yield u
  }
}
