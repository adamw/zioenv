package zioenv

import zio.{Task, ZIO, ZLayer}

object UserNotifier {
  // service
  trait Service {
    def notify(u: User, msg: String): Task[Unit]
  }

  // layer
  val live: ZLayer[Any, Nothing, UserNotifier] = ZLayer.succeed(new Service {
    override def notify(u: User, msg: String): Task[Unit] =
      Task {
        println(s"Sending $msg to ${u.email}")
      }
  })

  // accessor
  def notify(u: User, msg: String): ZIO[UserNotifier, Throwable, Unit] = ZIO.accessM(_.get.notify(u, msg))
}
