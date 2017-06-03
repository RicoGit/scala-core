package root.core.recursion

import scala.annotation.tailrec

/**
 * User: Constantine Solovev
 * Created: 03.06.17  18:37
 */


object Main extends App {

  // Fibonacci

  Fibonacci.fibonacciStreams.take(1000).foreach(println)

  (0 to 1000) foreach ( i => println(Fibonacci.fibonacciRecursion(i)) )

  (0 to 1000) foreach ( i => println(Fibonacci.fibonacciTailRecursion(i)) )


  // Factorial

  Factorial.factorialStreams.take(1000).foreach(println)

  (0 to 1000) foreach ( i => println(Factorial.factorialStreams(i)) )

  (0 to 1000) foreach ( i => println(Factorial.factorialTailRec(i)) )
}

case object Factorial {

  lazy val  factorialStreams: Stream[BigInt] =
    BigInt(1) #::
      factorialStreams.zipWithIndex.map { case (n, i) => n * (i + 1) }

  // not tail recursion
  def factorialRecursion(n: BigInt): BigInt = {
    if (n == 0) 1
    else n * factorialRecursion(n - 1)
  }

  // tail recursion impl
  @tailrec
  def factorialTailRec(n: BigInt, acc: => BigInt = 1): BigInt = {
    if (n == 0) acc
    else factorialTailRec(n - 1, n * acc)
  }

}

case object Fibonacci {

  lazy val fibonacciStreams: Stream[BigInt] =
      BigInt(0) #::
      BigInt(1) #::
      fibonacciStreams.zip(fibonacciStreams.tail).map { n => n._1 + n._2 }

  // tail not tail recursion (very slow)
  def fibonacciRecursion(n: BigInt): BigInt = {
    n match {
      case x: BigInt if x == 0 || x == 1 => n
      case y => fibonacciRecursion(n - 1) + fibonacciRecursion(n - 2)
    }
  }

  @tailrec
  def fibonacciTailRecursion(n: BigInt, a: BigInt = 0, b: BigInt = 1): BigInt = {
    if (n == 0) a
    else fibonacciTailRecursion(n - 1, b, a + b)
  }

}

