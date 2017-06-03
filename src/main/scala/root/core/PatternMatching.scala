package root.core

/**
 * User: Constantine Solovev
 * Created: 25.05.17  13:17
 */


object PatternMatching extends App {

  val x: List[Any] = List("str", "otherStr", 1, 2, 3, 5, 6, 7D, 8F, ("str", "2"), (10, "2"), List(1, 2, 3))

  x collect {
    case "str" => println("string str")
    case 1 | 2 | 3 => println("numbers from 1 to 3")
    case x @ (_: String, "2")  => println(s"tuple with first String elem and second elem=2 $x")
    case 1 :: second :: _ => println(s"list second elem $second, id first is 1")
    case _ => println("No one case matched")
  }

}
