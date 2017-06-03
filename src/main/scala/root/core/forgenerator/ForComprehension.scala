package root.core.forgenerator

import scala.collection.GenTraversableOnce

/**
 * User: Constantine Solovev
 * Created: 23.05.17  21:13
 */


object ForComprehension{

  class Children(val children: String*) {

    // required for "for with yield"
    def map(f: String => Int): Seq[Int] = {
      println("map")
      children.map(f)
    }

    // required for "for with yield"
    def flatMap(f: String => Seq[Int]): Seq[Int]  = {
      println("flatMap")
      children.flatMap(f)
    }

    // required for "for with condition"
    def withFilter(f: String => Boolean): Seq[String] = {
      println("withFilter")
      children.filter(f)
    }

    // required for "for without yield"
    def foreach(f: String => Unit): Unit = {
      println("foreach")
      children.foreach(f)
    }
  }


  def main(args: Array[String]): Unit = {
    val children = new Children("1", "2", "3")

    // used foreach
    for (child <- children ) println(child)

    // used withFilter
    val result1 = for (child <- children if child.nonEmpty) yield child.toInt * 2
    println(result1)

    // used map
    val result2 = for (child <- children) yield child.toInt * 2
    println(result2)

    // used flatMap
    val result3 = for (child <- children; char <- child) yield char.toInt * 2
    println(result3)

  }

}
