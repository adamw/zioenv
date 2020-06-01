import zio.Has

package object zioenv {
  type HasDBConfig = Has[DBConfig]
  type HasConnectionPool = Has[ConnectionPool]
  type DB = Has[DB.Service]
  type UserModel = Has[UserModel.Service]
  type UserNotifier = Has[UserNotifier.Service]
  type UserRegistration = Has[UserRegistration.Service]
}
