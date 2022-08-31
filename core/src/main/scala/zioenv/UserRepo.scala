package zioenv

import zio.*

trait UserRepo:
  def insert(u: User): Task[Unit]

object UserRepo:
  case class UserRepoImpl(db: DB) extends UserRepo:
    override def insert(u: User): Task[Unit] = db.execute(s"INSERT INTO user VALUES ('${u.name}')")

  val layer: ZLayer[DB, Nothing, UserRepo] = ZLayer {
    for db <- ZIO.service[DB]
    yield UserRepoImpl(db)
  }

  def insert(u: User): ZIO[UserRepo, Throwable, Unit] = ZIO.serviceWithZIO(_.insert(u))
