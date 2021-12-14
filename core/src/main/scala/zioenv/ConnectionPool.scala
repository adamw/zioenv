package zioenv

import zio.{ZIO, ZLayer, ZManaged}

// procedural interface
class ConnectionPool(url: String) {
  def close(): Unit = ()
  override def toString: String = s"ConnectionPool($url)"
}

// integration with ZIO
object ConnectionPoolIntegration {
  def createConnectionPool(dbConfig: DBConfig): ZIO[Any, Throwable, ConnectionPool] =
    ZIO.attempt(new ConnectionPool(dbConfig.url))
  val closeConnectionPool: ConnectionPool => ZIO[Any, Nothing, Unit] = (cp: ConnectionPool) =>
    ZIO.attempt(cp.close()).catchAll(_ => ZIO.unit)
  def managedConnectionPool(dbConfig: DBConfig): ZManaged[Any, Throwable, ConnectionPool] =
    ZManaged.acquireReleaseWith(createConnectionPool(dbConfig))(closeConnectionPool)

  val live: ZLayer[DBConfig, Throwable, ConnectionPool] =
    ZManaged.service[DBConfig].flatMap(dbConfig => managedConnectionPool(dbConfig)).toLayer
}
