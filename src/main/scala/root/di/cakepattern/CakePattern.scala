package root.di.cakepattern

/**
 * User: Constantine Solovev
 * Created: 03.06.17  21:58
 */


object CakePattern extends App {
  val service = new Service[User] with UserRepo with ConsoleLogger
  val result = service.getById("1")
  println(s"Result is $result")

}

// Logger

trait Logger {
  def info(msg: String)
}

trait ConsoleLogger extends Logger {
  override def info(msg: String): Unit = {
    println(s"INFO: $msg")
  }
}

// Repo

trait Repo[T] {
  def findById(id: String): T
  def findAll: List[T]
}
trait UserRepo extends Repo[User] {
  private val users = Map("1" -> User("1", "Rico"),"2" -> User("2", "Bob"))
  override def findById(id: String): User = users(id)
  override def findAll: List[User] = users.values.toList
}
case class User(id: String, name: String)

// Service

class Service[T] {
  // self-type annotation
  this: Repo[T] with Logger =>

  def getById(id: String): T = {
    info(s"get user by id=$id")
    findById(id)
  }
}
