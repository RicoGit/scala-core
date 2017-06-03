package root.di.manualinjection


/**
 * User: Constantine Solovev
 * Created: 13.02.17  22:49
 */

/** Manual injection */
object ConstructorInjection extends App {

  val service = new Service(new UserRepo(), new ConsoleLogger())
  val result = service.getById("1")
  println(s"Result is $result")

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
  private val users = Map("1" -> User("1", "Rico"), "2" -> User("2", "Bob"))

  override def findById(id: String): User = users(id)

  override def findAll: List[User] = users.values.toList
}

case class User(id: String, name: String)

// Service

class Service[T](repo: Repo[T], logger: Logger) {
  def getById(id: String): T = {
    logger.info(s"get user by id=$id")
    repo.findById(id)
  }
}