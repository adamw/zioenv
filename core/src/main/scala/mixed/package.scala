import zio.Has

package object mixed {
  type HasDBConfig = Has[DBConfig]
  type HasConnectionPool = Has[ConnectionPool]
  type DB = Has[DB.Service]
}
