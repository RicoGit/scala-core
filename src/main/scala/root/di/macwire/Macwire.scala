package root.di.macwire

import com.softwaremill.macwire._

/**
 * Thin cake pattern with Macwire.
 */
object Macwire extends App {
  val cxt = new ServiceCxt()
  println(cxt.service.getById("1"))
}

class ServiceCxt {
  lazy val logger = wire[ConsoleLogger]
  lazy val repo = wire[UserRepo]
  lazy val service = wire[UserService]
}


// Logger

trait Logger {
  def info(msg: String)
}

class ConsoleLogger extends Logger {
  override def info(msg: String): Unit = {
    println(s"INFO: $msg")
  }
}

// Repo

trait Repo[T] {
  def findById(id: String): T
  def findAll: List[T]
}
class UserRepo extends Repo[User] {
  private val users = Map("1" -> User("1", "Rico"),"2" -> User("2", "Bob"))
  override def findById(id: String): User = users(id)
  override def findAll: List[User] = users.values.toList
}
case class User(id: String, name: String)

// Service

trait Service[T] {
  def getById(id: String): T
}

class UserService(logger: Logger, repo: UserRepo) extends Service[User]{
  def getById(id: String): User = {
    logger.info(s"get user by id=$id")
    repo.findById(id)
  }
}
