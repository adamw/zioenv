package zioenv

import zio.Task

trait UserNotifier {
  def notify(u: User, msg: String): Task[Unit]
}

object DefaultUserNotifier extends UserNotifier {
  override def notify(u: User, msg: String): Task[Unit] =
    Task {
      println(s"Sending $msg to ${u.email}")
    }
}
