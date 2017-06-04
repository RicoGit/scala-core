package root.core.other.change.impl.case2

import root.core.other.change.impl.case2.ContextPredicate.{AndCxtPredicate, ContainsCxtPredicate, CxtPredicate, OrCxtPredicate}


/**
 * User: Constantine Solovev
 * Created: 04.06.17  6:50
 *
 * Change behavior of multilevel object with once rebuilding object.
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

  trait CxtPredicate {
    def cxtApply(data: String): (Boolean, Seq[String])
  }

  // wrappers for custom logics

  class ContainsCxtPredicate(predicate: ContainsPredicate) extends CxtPredicate {
    override def cxtApply(data: String): (Boolean, Seq[String]) = {
      predicate(data) match {
        case true => true -> Seq(predicate.key)
        case _ => false -> Seq()
      }
    }
  }

  class AndCxtPredicate(left: CxtPredicate, right: CxtPredicate) extends CxtPredicate {
    override def cxtApply(data: String): (Boolean, Seq[String]) = {
      left.cxtApply(data) -> right.cxtApply(data) match {
        case ((true, cxt1), (true, cxt2)) => true -> (cxt1 ++ cxt2)
        case _ => false -> Seq()
      }
    }
  }

  class OrCxtPredicate(left: CxtPredicate, right: CxtPredicate) extends CxtPredicate {
    override def cxtApply(data: String): (Boolean, Seq[String]) = {
      left.cxtApply(data) -> right.cxtApply(data) match {
        case ((true, cxt1), (true, cxt2)) => true -> (cxt1 ++ cxt2)
        case ((true, cxt), (false, _)) => true -> cxt
        case ((false, _), (true, cxt)) => true -> cxt
        case _ => false -> Seq()
      }
    }
  }

}

// change big composite object behavior once - rebuild object
object RebuildObjectOnFirstCall extends App {

  // methods for rebuilding object
  def wrap(predicate: Predicate): CxtPredicate = {
    predicate match {
      case p: ContainsPredicate =>
        new ContainsCxtPredicate(p)
      case p: AndPredicate =>
        new AndCxtPredicate(wrap(p.left), wrap(p.right))
      case p: OrPredicate =>
        new OrCxtPredicate(wrap(p.left), wrap(p.right))
      case p => throw new IllegalArgumentException(s"Unknown predicate $p")
    }
  }

  val aPredicate = new ContainsPredicate("a")
  val bPredicate = new ContainsPredicate("b")
  val cPredicate = new ContainsPredicate("c")
  val dPredicate = new ContainsPredicate("d")
  val and1Predicate = new AndPredicate(aPredicate, bPredicate)
  val orPredicate = new OrPredicate(cPredicate, and1Predicate)
  val and2Predicate = new AndPredicate(orPredicate, dPredicate)

  val data = "test a and b and d"

  val cxtPredicate = wrap(and2Predicate)

  println(cxtPredicate.cxtApply(data))

  val start = System.nanoTime()
  for (x <- 1 to 10000000) {
    cxtPredicate.cxtApply(data)
  }
  val middle = System.nanoTime()
  for (x <- 1 to 10000000) {
    orPredicate.apply(data)
  }

  val end = System.nanoTime()

  // very naive benchmarks )

  println(middle - start)
  println(end - middle)
  println((middle - start) / (end - middle) + " times slower")

  // direct call faster in a 10-15 times

}
