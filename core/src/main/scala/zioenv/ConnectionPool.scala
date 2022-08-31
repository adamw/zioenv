package zioenv

import zio.*

// procedural interface
trait ConnectionPool:
  def close(): Unit

object ConnectionPool:

  case class ConnectionPoolImpl(url: String) extends ConnectionPool:
    def close(): Unit = ()
    override def toString: String = s"ConnectionPool($url)"

// integration with ZIO
  def createConnectionPool(dbConfig: DBConfig): ZIO[Any, Throwable, ConnectionPool] =
    ZIO.attempt(new ConnectionPoolImpl(dbConfig.url))
  val closeConnectionPool: ConnectionPool => ZIO[Any, Nothing, Unit] = (cp: ConnectionPool) =>
    ZIO.attempt(cp.close()).catchAll(_ => ZIO.unit)
  def managedConnectionPool(dbConfig: DBConfig): ZIO[Scope, Throwable, ConnectionPool] =
    ZIO.acquireRelease(createConnectionPool(dbConfig))(closeConnectionPool)

  val layer: ZLayer[DBConfig, Throwable, ConnectionPool] = ZLayer.scoped {
    ZIO.service[DBConfig].flatMap(dbConfig => managedConnectionPool(dbConfig))
  }
