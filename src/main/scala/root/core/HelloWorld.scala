package root.core

/**
 * User: Constantine Solovev
 * Created: 26.05.17  23:56
 */
object HelloWorld extends App {

  println("Hello world!")

  val input = "test"

  input match {
    case "wrong" =>
      println("wrong")
    case `input`  =>
      println("right")
  }

}
