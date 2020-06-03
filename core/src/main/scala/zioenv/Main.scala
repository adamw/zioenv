package zioenv

import zio._

object Main extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val program: ZIO[UserRegistration, Throwable, User] =
      UserRegistration.register(User("adam", "adam@hello.world"))

    val dbLayer: ZLayer[Any, Throwable, DB] = ZLayer.succeed(DBConfig("jdbc://localhost")) >>>
      ConnectionPoolIntegration.live >>>
      DB.liveRelationalDB

    val userRegistrationLayer: ZLayer[Any, Throwable, UserRegistration] =
      ((dbLayer >>> UserModel.live) ++ UserNotifier.live) >>> UserRegistration.live

    program
      .provideLayer(userRegistrationLayer)
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { u =>
        println(s"Registered user: $u (layers)")
        ExitCode.success
      }
  }
}
