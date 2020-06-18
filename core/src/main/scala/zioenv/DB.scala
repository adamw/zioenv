package zioenv

import zio.Task

trait DB {
  def execute(sql: String): Task[Unit]
}

class RelationalDB(cp: ConnectionPool) extends DB {
  override def execute(sql: String): Task[Unit] =
    Task {
      println(s"Running: $sql, on: $cp")
    }
}
