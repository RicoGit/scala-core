package root.core.generics

/**
 * User: Constantine Solovev
 * Created: 23.05.17  10:58
 */


object Inheritance {

  class Parent
  class Child extends Parent

  abstract class Box[A](elem: A)
  class ParentBox(child: Parent) extends Box[Parent](child)
  class ChildBox(parent: Child) extends Box[Child](parent)

  abstract class Factory[A](box: Box[A])
  class ParentFactory(box: Box[Parent]) extends Factory[Parent](box)
  class ChildFactory(box: Box[Child]) extends Factory[Child](box)


  val child = new Child()
  val childBox = new ChildBox(child)
  val childFactory = new ChildFactory(childBox)


  val parent = new Parent()
  val parentBox = new ParentBox(parent)
  val parendFactory = new ParentFactory(parentBox)

}
