package root.core.generics

import scala.runtime.Nothing$
import scala.util.Random

/**
 * User: Constantine Solovev
 * Created: 22.05.17  21:32
 */


object Variance {

  class Parent(name: String)
  class Child(name: String) extends Parent(name)

  /* co-variance (in java ? extends A)*/

  trait Provider[+A] {
    val elem: A
    def get(): A = {
      println(s"Provider gave a elem=$elem")
      elem
    }
  }

  var parentProvider: Provider[Parent] = new Provider[Parent]() { val elem = new Parent("Bob") }
  var childProvider:  Provider[Child] = new Provider[Child]() { val elem = new Child("Rob") }

// allowed
  parentProvider = childProvider
// not allowed
//  childProvider = parentProvider



  /* contra-variance (in java ? super A)*/

  trait Consumer[-A] {
    def put(elem: A): Unit = {
      println(s"consumer got a elem=$elem")
    }
  }

  var parentConsumer: Consumer[Parent] = new Consumer[Parent]() {}
  var childConsumer:  Consumer[Child] = new Consumer[Child]() {}

// allowed
  childConsumer = parentConsumer
// not allowed
//  parentConsumer = childConsumer


  /* both cases in one class */

  class Transformer[-A, +B] {
    def transform(a: A): B = ???
  }

  def main(args: Array[String]): Unit = {
    val transformer = new Transformer[String, Int]() {
      override def transform(a: String): Int = a.length
    }

    println(transformer.transform("testString "))


  }

}


