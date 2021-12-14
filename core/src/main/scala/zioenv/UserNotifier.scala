package zioenv

import zio.{Clock, Task, ZIO}

object UserNotifier {
  def notify(u: User, msg: String): ZIO[Clock, Throwable, Unit] = {
    Clock.currentDateTime.flatMap { dateTime =>
      Task {
        println(s"Sending $msg to ${u.email} @ $dateTime")
      }
    }
  }
}
