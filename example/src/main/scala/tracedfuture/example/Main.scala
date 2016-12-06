package trackedfuture.example

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object Main {

  def main(args: Array[String]): Unit = {

    val f = f0("222")
    try {
      val r = Await.result(f, 10 seconds)
    } catch {
      // will print with f0 when agent is enabled
      case ex: Throwable => ex.printStackTrace
    }
    try {
      val r = Await.result(f3("AAA"), 10 seconds)
    } catch {
      case ex: Throwable => ex.printStackTrace
    }
  }

  def f0(x: String): Future[Unit] = {
    f1(x)
  }

  def f1(x: String): Future[Unit] =
    Future {
      throw new RuntimeException("AAA");
    }

  def f3(x: String): Future[Unit] = f4(x)

  def f4(x: String): Future[Unit] = {
    Future {
      "aaaa "
    } map { _ => throw new Exception("bbb-q1") }
  }

  def fFlatMap0(): Future[Unit] = fFlatMap1()

  def fFlatMap1(): Future[Unit] = {
    Future {
      Future {
        "aaaa "
      }
    } flatMap { _ => throw new Exception("bbb-q1") }
  }

  def fFilter0(): Future[Unit] = fFilter1()

  def fFilter1(): Future[Unit] = {
    Future {
      ()
    } filter { _ => false }
  }

  def withFilter0(): Future[Unit] = withFilter1()

  def withFilter1(): Future[Unit] = {
    Future {
      ()
    } withFilter { _ => throw new Exception("AAA") }
  }

  def fCollect0(f: PartialFunction[String, String]): Future[String] =
    fCollect1(f)

  def fCollect1(f: PartialFunction[String, String]): Future[String] =
    Future {
      "aaa"
    } collect f

  def fOnComplete0(ec: ExecutionContext): Unit =
    fOnComplete1(ec)

  def fOnComplete1(ec: ExecutionContext): Unit = {
    Future {
      "aaa"
    }.onComplete { _ => throw new RuntimeException("Be-Be-Be!") }(ec)
  }

  def fOnFailure0(ec: ExecutionContext): Unit = {
    fOnFailure1(ec)
  }

  def fOnFailure1(ec: ExecutionContext): Unit = {
    Future(1 / 0).onFailure { case ex: ArithmeticException => throw new RuntimeException("from onFailure") }(ec)
  }

  def fOnSuccess0(ec: ExecutionContext): Unit = {
    fOnSuccess1(ec)
  }

  def fOnSuccess1(ec: ExecutionContext): Unit = {
    Future(1).onSuccess { case 1 => throw new RuntimeException("from onSuccess") }(ec)
  }

  def fForeach0(ec: ExecutionContext): Unit =
    fForeach1(ec)

  def fForeach1(ec: ExecutionContext): Unit = {
    Future {
      "aaa"
    }.foreach { _ => throw new RuntimeException("Be-Be-Be!") }(ec)
  }

  def fTransform0(): Future[Int] =
    fTransform1

  def fTransform1(): Future[Int] = {
    Future {
      "aaa"
    }.transform((_.toInt), identity)
  }

  def fRecover0(): Future[Int] =
    fRecover1

  def fRecover1(): Future[Int] = {
    Future {
      "aaa"
    }.map((_.toInt)).recover {
      case ex: NumberFormatException => throw new RuntimeException("from recover")
    }
  }

  def fRecoverWith0(): Future[Int] = fRecoverWith1

  def fRecoverWith1(): Future[Int] =
    Future(1 / 0).recoverWith {
      case ex: ArithmeticException => throw new RuntimeException("from recoverWith")
    }

  def awaitReady0(ec: ExecutionContext) = {
    awaitReady1(ec)
  }

  def awaitReady1(ec: ExecutionContext) = {
    val f = Future {
      Thread.sleep(10 * 1000)
    }(ec)

    Await.ready(f, 1000 millis)
  }

  def awaitResult0(ec: ExecutionContext) = {
    awaitResult1(ec)
  }

  def awaitResult1(ec: ExecutionContext) = {
    val f = Future {
      Thread.sleep(10 * 1000)
    }(ec)

    Await.result(f, 1000 millis)
  }


}
