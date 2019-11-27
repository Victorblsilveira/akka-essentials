package part1recap

import scala.util.Try

object GeneralRecap extends App {
  val aCondition: Boolean = false

  var aVariable = 42
  aVariable += 1


  val aConditionedVal = if (aCondition) 42 else 65

  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  val theUnit = println("Hello,  Scala")

  def aFunction(x: Int): Int = x + 1

  def factorial(n: Int, acc: Int): Int =
    if (n <= 0) acc
    else factorial(n - 1, acc * n)

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // Methods notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  //anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  aCarnivore eat aDog

  //Generics
  abstract class  MyList[+A]
  // Companion objets
  object MyList


  case class Person(name: String, age: Int)


  val aPotentialFailure = try {
    throw  new RuntimeException("")
  } catch {
    case e: Exception => "omg, an exception"
  } finally  {
    println("kaboomm")
  }

  // Functional programing
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  val incremented = incrementer(42)
  // incrementer.apply(42)

  val anonymousIncrementer = (x: Int) => x + 1
  // Int => Int === Function[Int, Int]

  List(1,2,3).map(incrementer)

  val pairs  = for {
    num <- List(1,2,3,4)
    char <- List('a','b','c','d')
  } yield num + "-" + char
  // List(1,2,3,4).flatMap(num => List('a','b','c','d').map(char => num + "-" + char))

  println(pairs)

  val anOption = Some(2)
  val aTry = Try {
    throw new RuntimeException
  }

  val unknown = 2
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "unknown"
  }

  val bob = Person("bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"hi, my name is $n"
    case _ => "I don't know my name"
  }
  //All the patterns

}
