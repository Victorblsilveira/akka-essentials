package part1recap

import scala.concurrent.Future

object ThreadModelLimitations extends App {
  /*
    Daniel's rants
  */

  /**
   * DR #1: OOP ecapsulation is only valid in the SINGLE THREADED MODEL.
   */
  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount
    def withdraw(money: Int) = this.amount -= money // this.synchronizes { this.amount -= money }
    def deposit(money: Int) =  this.amount += money // this.synchronizes { this.amount += money }
    def getAmount = amount
  }

  val account  = new BankAccount(2000)

  for (_ <- 1 to 1000) {
    new Thread(()=> account.withdraw(1)).start()
  }

  for (_ <- 1 to 1000) {
    new Thread(()=> account.deposit(1)).start()
  }
  println(account.getAmount)

  // OOP encapsulation is broken in a multithreaded env
  // synchronization ! Locks to the rescues

  // deadlocks, livelocks

  /**
   * DR #2: Delegating something to a thread is a PAIN.
   */

  // executor service
  // you have a running thread and you want to pass a runnable to that thread.

  var task: Runnable = null

  val runningThread:  Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task...")
          runningThread.wait()
        }

        task.synchronized {
          println("[background] I have a task!")
          task.run()
          task = null
        }
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable): Unit = {
    if(task == null) task = r

    runningThread.synchronized {
      runningThread.notify()
    }
  }


  runningThread.start()
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println(42))
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("This should run in the background"))


  /**
   * DR #3: Tracing and dealing with errors in a multithreaded env is a PITN.
   * */

  // 1M number between 10 threads

  import scala.concurrent.ExecutionContext.Implicits.global
  val futures = (1 to 10)
    .map(i => 100000 * i until 100000 * (i + 1))
    .map(range => Future {
      if (range.contains(546735)) throw new RuntimeException("Invalid Number")
      range.sum
    })

  val sumFuture = Future.reduceLeft(futures)(_ + _) // Future with the sum of all the numbers

  sumFuture.onComplete(println)
}
