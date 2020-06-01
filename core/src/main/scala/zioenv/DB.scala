package zioenv

import zio.{Task, ZIO, ZLayer}

object DB {
  // service
  trait Service {
    def execute(sql: String): Task[Unit]
  }

  // layer
  val liveRelationalDB: ZLayer[HasConnectionPool, Throwable, DB] = ZLayer.fromService { cp =>
    new Service {
      override def execute(sql: String): Task[Unit] =
        Task {
          println(s"Running: $sql, on: $cp")
        }
    }
  }

  // accessor
  def execute(sql: String): ZIO[DB, Throwable, Unit] = ZIO.accessM(_.get.execute(sql))
}
