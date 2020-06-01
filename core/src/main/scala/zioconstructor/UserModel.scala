package zioconstructor

import zio.Task

trait UserModel {
  def insert(u: User): Task[Unit]
}

// service implementation
class DefaultUserModel(db: DB) extends UserModel {
  override def insert(u: User): Task[Unit] = db.execute(s"INSERT INTO user VALUES ('${u.name}')")
}
