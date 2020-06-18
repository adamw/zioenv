package zioenv

import zio.{ZIO, ZManaged}

// procedural interface
class ConnectionPool(url: String) {
  def close(): Unit = ()
  override def toString: String = s"ConnectionPool($url)"
}

// integration with ZIO
object ConnectionPoolIntegration {
  def createConnectionPool(dbConfig: DBConfig): ZIO[Any, Throwable, ConnectionPool] =
    ZIO.effect(new ConnectionPool(dbConfig.url))
  val closeConnectionPool: ConnectionPool => ZIO[Any, Nothing, Unit] = (cp: ConnectionPool) =>
    ZIO.effect(cp.close()).catchAll(_ => ZIO.unit)
  def managedConnectionPool(dbConfig: DBConfig): ZManaged[Any, Throwable, ConnectionPool] =
    ZManaged.make(createConnectionPool(dbConfig))(closeConnectionPool)
}
