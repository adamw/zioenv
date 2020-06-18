package zioenv

import zio._

object Main extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    ConnectionPoolIntegration
      .managedConnectionPool(DBConfig("jdbc://localhost"))
      .use { cp =>
        lazy val db = new RelationalDB(cp)
        lazy val userModel = new DefaultUserModel(db)
        lazy val userRegistration = new UserRegistration(DefaultUserNotifier, userModel)
        userRegistration.register(User("adam", "adam@hello.world"))
      }
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { u =>
        println(s"Registered user: $u (constructors)")
        ExitCode.success
      }
  }
}
