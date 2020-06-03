package mixed

import zio._

object Main extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val program: ZIO[Has[UserRegistration], Throwable, User] =
      ZIO.accessM[Has[UserRegistration]](_.get.register(User("adam", "adam@hello.world")))

    val dbLayer: ZLayer[Any, Throwable, DB] = ZLayer.succeed(DBConfig("jdbc://localhost")) >>>
      ConnectionPoolIntegration.live >>>
      DB.liveRelationalDB

    val userRegistrationLayer: ZLayer[Any, Throwable, Has[UserRegistration]] =
      dbLayer.map { db =>
        lazy val userModel = new DefaultUserModel(db.get)
        lazy val userRegistration = new UserRegistration(DefaultUserNotifier, userModel)
        Has(userRegistration)
      }

    program
      .provideLayer(userRegistrationLayer)
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { u =>
        println(s"Registered user: $u (layers)")
        ExitCode.success
      }
  }
}
