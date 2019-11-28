package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MultiThreadindRecap extends App {

  // creating threads on the JVM

  val aThread = new Thread(() => println("I'am running in parallel"))
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 1000).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 1000).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodBye.start()
  // different runs produce different results!

  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount
    def withdraw(money: Int) = this.amount -= money

    def safeWithdraw(money: Int) = this.synchronized {
      this.amount -= money
    }
  }

  /*
    BA(10000)

    T1 -> withdraw 1000
    T2 -> withdraw 2000

    T1 -> this.amount = this.amount - ..... // PREEMPTED by the 0S
    T2 -> this.amount = this.amount - 2000 = 8000
    T1 -> -1000 = 9000

    => RESULT = 9000

    THIS.AMOUNT = THIS.AMOUNT -1000 IS NOT ATOMIC

  */

  // inter-thread communication on the JVM
  // wait - notify mechanism

  //Scala Futures
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    // long coputation -on a different thread
    42
  }

  // callbacks
  future.onComplete {
    case Success(_) => println("I found the meaning of life")
    case Failure(_) => println("something happened with the meaning of life")
  }

  val aProcessedFuture = future.map(_ + 1) // Future with 43
  val aFlatFuture = future.flatMap { value => Future(value+2) }
  val filteredFuture = future.filter(_ % 2 == 0) // NoSuchElementException

  // for comprehesions
  val aNonSenseFuture = for {
    meaningofLife <- future
    filteredMeaning <- filteredFuture
  } yield meaningofLife + filteredMeaning

  // andThen, recover/recoverWith


  // Promises
  
}
