package zioenv

import zio.{Task, ZIO, ZLayer}

object UserModel {

  // service
  trait Service {
    def insert(u: User): Task[Unit]
  }

  // layer - service implementation
  val live: ZLayer[DB, Nothing, UserModel] = ZLayer.fromService { db =>
    new Service {
      override def insert(u: User): Task[Unit] = db.execute(s"INSERT INTO user VALUES ('${u.name}')")
    }
  }

  // accessor
  def insert(u: User): ZIO[UserModel, Throwable, Unit] = ZIO.accessM(_.get.insert(u))
}
