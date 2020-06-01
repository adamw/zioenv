package zioenv

import zio.{Task, ZIO, ZLayer}

object UserRegistration {
  // service - alternative definition
  class Service(notifier: UserNotifier.Service, userModel: UserModel.Service) {
    def register(u: User): Task[User] = {
      for {
        _ <- userModel.insert(u)
        _ <- notifier.notify(u, "Welcome!")
      } yield u
    }
  }

  // layer
  val live: ZLayer[UserNotifier with UserModel, Nothing, UserRegistration] =
    ZLayer.fromServices[UserNotifier.Service, UserModel.Service, UserRegistration.Service](
      new Service(_: UserNotifier.Service, _: UserModel.Service)
    )

  // accessor
  def register(u: User): ZIO[UserRegistration, Throwable, User] = ZIO.accessM(_.get.register(u))
}
