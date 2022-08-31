package zioenv

import zio.{Task, ZIO, ZLayer}

trait DB:
  def execute(sql: String): Task[Unit]

object DB:
  // implementation
  case class RelationalDB(cp: ConnectionPool) extends DB:
    override def execute(sql: String): Task[Unit] =
      ZIO.succeed {
        println(s"Running: $sql, on: $cp")
      }

  // layer
  val layer: ZLayer[ConnectionPool, Throwable, DB] = ZLayer {
    for cp <- ZIO.service[ConnectionPool]
    yield RelationalDB(cp)
  }

  // accessor
  def execute(sql: String): ZIO[DB, Throwable, Unit] = ZIO.serviceWithZIO(_.execute(sql))
