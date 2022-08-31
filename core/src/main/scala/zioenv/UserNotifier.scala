package zioenv

import zio.*

trait UserNotifier:
  def notify(u: User, msg: String): Task[Unit]

object UserNotifier:

  case class UserNotifierimpl() extends UserNotifier:
    override def notify(u: User, msg: String): Task[Unit] = {
      for dateTime <- Clock.currentDateTime
      yield ZIO.succeed {
        println(s"Sending $msg to ${u.email} @ $dateTime")
      }
    }

  val layer = ZLayer.succeed(UserNotifierimpl())

  def notify(u: User, msg: String): ZIO[UserNotifier, Throwable, Unit] =
    ZIO.serviceWithZIO[UserNotifier](_.notify(u, msg))
