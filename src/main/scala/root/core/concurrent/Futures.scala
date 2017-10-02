package root.core.concurrent

import java.util.concurrent.Executors

import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutorService, Future, Promise}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * User: Constantine Solovev
 * Created: 02.10.17  17:09
 */


object Futures {

  def main(args: Array[String]): Unit = {

    // Future

    val future1 = doSomething().map(_ + " Well done!")

    doSomething().onComplete {
      case Success(str) => println(str)
      case Failure(ex) => println(ex)
    }

    Await.ready(future1, Duration.Inf).foreach(println)

    Thread.sleep(2000)

    // Promise

    implicit val executionContext: ExecutionContextExecutorService =
      ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

    val promise = Promise[String]()

    Future {
      Thread.sleep(1000)
      promise.success("promise was fulfilled")
    }

    Await.ready(promise.future, Duration.Inf).foreach(println)

  }

  def doSomething(): Future[String] = Future {
    Thread.sleep(1000)
    "I did!"
  }

}
