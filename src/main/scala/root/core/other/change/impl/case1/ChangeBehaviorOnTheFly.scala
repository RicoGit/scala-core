package root.core.other.change.impl.case1

import root.core.other.change.impl.case1.ContextPredicate.CxtPredicate

/**
 * User: Constantine Solovev
 * Created: 04.06.17  6:50
 *
 * Change behavior of multilevel object with implicit wrapping on the fly.
 */


// Base final classes, witch should change functionality

sealed trait Predicate {
  def apply(key: String): Boolean
}

final class ContainsPredicate(val key: String) extends Predicate {
  override def apply(data: String): Boolean = data.contains(key)
}

final class AndPredicate(val left: Predicate, val right: Predicate) extends Predicate {
  override def apply(data: String): Boolean = left(data) && right(data)
}

final class OrPredicate(val left: Predicate, val right: Predicate) extends Predicate {
  override def apply(data: String): Boolean = left(data) || right(data)
}

// predicated with context - enrich base classes

object ContextPredicate {

  trait CxtPredicate[T] {
    def cxtApply(data: String, predicate: T): (Boolean, Seq[String])
  }

  object CxtPredicate {

    // for implicits will be found in companion object of trait CxtPredicate

    implicit object CxtPredicate extends CxtPredicate[Predicate] {
      override def cxtApply(data: String, predicate: Predicate): (Boolean, Seq[String]) = {
        predicate match {
          case p: ContainsPredicate => ContainsCxtPredicate.cxtApply(data, p)
          case p: AndPredicate =>  AndCxtPredicate.cxtApply(data, p)
          case p: OrPredicate =>  OrCxtPredicate.cxtApply(data, p)
        }
      }
    }

    object ContainsCxtPredicate extends CxtPredicate[ContainsPredicate] {
      override def cxtApply(data: String, predicate: ContainsPredicate): (Boolean, Seq[String]) = {
        predicate(data) match {
          case true => true -> Seq(predicate.key)
          case _ => false -> Seq()
        }
      }
    }

    object AndCxtPredicate extends CxtPredicate[AndPredicate] {
      override def cxtApply(data: String, predicate: AndPredicate): (Boolean, Seq[String]) = {
        applyWithCxt(data, predicate.left) -> applyWithCxt(data, predicate.right) match {
          case ((true, cxt1), (true, cxt2)) => true -> (cxt1 ++ cxt2)
          case _ => false -> Seq()
        }
      }
    }

    object OrCxtPredicate extends CxtPredicate[OrPredicate] {
      override def cxtApply(data: String, predicate: OrPredicate): (Boolean, Seq[String]) = {
        applyWithCxt(data, predicate.left) -> applyWithCxt(data, predicate.right) match {
          case ((true, cxt1), (true, cxt2)) => true -> (cxt1 ++ cxt2)
          case ((true, cxt), (false, _)) => true -> cxt
          case ((false, _), (true, cxt)) => true -> cxt
          case _ => false -> Seq()
        }
      }
    }

    def applyWithCxt[T <: Predicate : CxtPredicate](data: String, predicate: T): (Boolean, Seq[String]) = {
      implicitly[CxtPredicate[T]].cxtApply(data, predicate)
    }

  }
}

// change big composite object behavior on the fly
object ChangeBehaviorOnTheFly extends App {

  val aPredicate = new ContainsPredicate("a")
  val bPredicate = new ContainsPredicate("b")
  val cPredicate = new ContainsPredicate("c")
  val dPredicate = new ContainsPredicate("d")
  val and1Predicate = new AndPredicate(aPredicate, bPredicate)
  val orPredicate = new OrPredicate(cPredicate, and1Predicate)
  val and2Predicate = new AndPredicate(orPredicate, dPredicate)

  val data = "test a and b"

  println(CxtPredicate.applyWithCxt(data, and2Predicate: Predicate))

  val start = System.nanoTime()

  for (x <- 1 to 1000000) {
    CxtPredicate.applyWithCxt(data, and2Predicate: Predicate)
  }

  val middle = System.nanoTime()

  for (x <- 1 to 1000000) {
    orPredicate.apply(data)
  }

  val end = System.nanoTime()

  // very naive benchmarks )

  println(middle-start)
  println(end-middle)
  println((middle-start) / (end-middle) + " times slower")

  // direct call faster in a 15-30 times

}
