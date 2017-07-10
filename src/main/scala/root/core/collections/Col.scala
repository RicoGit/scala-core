package root.core.collections

import scala.language.postfixOps

/**
 * User: Constantine Solovev
 * Created: 25.05.17  12:32
 */


object Col extends App{


  // collect

  val res1 = List(0, 1, 2, 3) collect {
    case 1 => "one"
    case 2 => "two"
    case 3 => "three"
  }

  val res2 = 0 to 100 collect Map(
    1 -> "one",
    2 -> "two",
    3 -> "three"
  )

  val res3 = Array(0, 1, 2, 3) collectFirst {
    case x if x % 2 == 0 => "str"
  }

  println(res3)

  // reduce

  val list = List(1, 2, 3, 4)


  // should use only associative operators with "reduce"
  list.reduce(_ - _)
  list.reduceLeft(_ - _)
  list.reduceRight(_ - _)

  list.reduceOption(_ - _)
  list.reduceLeftOption(_ - _)
  list.reduceRightOption(_ - _)


  println(res1, res2)

  // Don’t compute full length for length matching, use lengthCompare
  list.lengthCompare(2) > 2

  val x: Option[Int] = list.lift(3)

  x

  // foo baz bar

  val input = (0 to 10).toSeq

  val result1 = input collect {
    case x if (x % 6) == 0 => Some("foobar")
    case x if (x % 2) == 0 => Some("foo")
    case x if (x % 3) == 0 => Some("bar")
    case _ => None
  } flatten

  val result2 = for {
    i <- input
    result <- i % 6 match {
      case 0 => Some("foobar")
      case 2 | 4 => Some("foo")
      case 3 => Some("bar")
      case _ => None
    }
  } yield result

  val result3 = input.flatMap(i =>
    if (i % 6 == 0) {
      Some("foobar")
    } else if (i % 2 == 0) {
      Some("foo")
    } else if (i % 3 == 0) {
      Some("bar")
    } else {
      None
    }

  )

  println(result1)
  println(result2)
  println(result3)
}
