package zioenv

import zio.*

object Main extends ZIOAppDefault:

  def run =
    UserRegistration
      .register(User("adam", "adam@hello.world"))
      .map { u => println(s"Registered user: $u (layers)") }
      .provide(
        ConnectionPool.layer,
        UserRegistration.layer,
        UserRepo.layer,
        UserNotifier.layer,
        DB.layer,
        ZLayer.succeed(DBConfig("jdbc://localhost"))
      )
