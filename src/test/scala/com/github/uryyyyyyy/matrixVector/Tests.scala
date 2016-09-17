package com.github.uryyyyyyy.matrixVector

import breeze.linalg._
import com.github.fommil.netlib.BLAS
import org.scalatest.{FunSpec, MustMatchers}

import scala.util.Random

object Timer{
  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ms")
    result
  }
}

class Tests extends FunSpec with MustMatchers {

  def makeRandomMatrixAndVector(row: Int, column:Int): (Array[Array[Double]], Array[Double]) ={
    val random = new Random()
    def elm() = Array.fill(column)(random.nextDouble())
    val matrix_A = Array.fill(row)(elm())
    val vector_B = Array.fill(column)(random.nextDouble())

    (matrix_A, vector_B)
  }

  describe("MatrixVectorSampleTest") {

    it("breeze sample1") {
      val matrix_A = DenseMatrix(Array(1.0, 2.0, 3.0), Array(4.0, 5.0, 6.0))
      println("matrix_A")
      println(matrix_A)
      val vector_B = DenseMatrix(Array(1.0), Array(2.0), Array(3.0))
      println("\nvector_B")
      println(vector_B)
      val result = matrix_A * vector_B
      println("\nresult")
      println(result)
    }

    it("breeze sample2") {
      val (a, b) = makeRandomMatrixAndVector(10, 3)

      val matrix_A = DenseMatrix(a :_*)
      println("matrix_A")
      println(matrix_A)
      val vector_B = DenseMatrix(b :_*)
      println("vector_B")
      println(vector_B)

      val result = matrix_A * vector_B
      println("result")
      println(result)
    }

    it("netlib ddot sample1") {
      val blas: BLAS = BLAS.getInstance

      val (a, b) = makeRandomMatrixAndVector(10, 3)

      val vector_A = Array(1.0, 2.0, 3.0)
      println("vector_A")
      println(vector_A.toSeq.mkString(","))

      val vector_B = Array(4.0, 5.0, 6.0)
      println("vector_B")
      println(vector_B.toSeq.mkString(","))

      val dotProduct = blas.ddot(vector_A.length, vector_A, 1, vector_B, 1)
      println("dotProduct")
      println(dotProduct)
    }

    it("netlib ddot sample2") {
      val blas: BLAS = BLAS.getInstance

      val (a, vector_B) = makeRandomMatrixAndVector(10, 3)

      println("matrix_A")
      a.foreach(elm => println(elm.toSeq.mkString(",")))

      println("\nvector_B")
      vector_B.foreach(println)

      val result = a.map(vector => blas.ddot(vector.length, vector, 1, vector_B, 1))
      println("\nresult")
      result.foreach(println)
    }

    it("netlib dgemv sample1") {
      val blas: BLAS = BLAS.getInstance

      val m = 3
      val n = 2

      val matrix_A = new Array[Double](m* n)

      matrix_A(0)=1; matrix_A(3)=2
      matrix_A(1)=0; matrix_A(4)=2
      matrix_A(2)=3; matrix_A(5)=3

      val vector_B = new Array[Double](n)
      vector_B(0)=1
      vector_B(1)=2

      val result = new Array[Double](m)

      blas.dgemv("N", m, n, 1.0, matrix_A, m, vector_B, 1, 0.0, result, 1)

      println("\nresult")
      result.foreach(println)
    }

    it("netlib dgemv sample2") {
      val blas: BLAS = BLAS.getInstance
      val m = 10
      val n = 3
      val (a, vector_B) = makeRandomMatrixAndVector(m, n)

      println("matrix_A")
      a.foreach(elm => println(elm.toSeq.mkString(",")))

      val matrix_A = new Array[Double](m* n)
      for{
        (elm, i) <- a.zipWithIndex
        (score, j) <- elm.zipWithIndex
      }{matrix_A(i + j * m) = score}

      println("\nvector_B")
      vector_B.foreach(println)

      val result = new Array[Double](m)
      blas.dgemv("N", m, n, 1.0, matrix_A, m, vector_B, 1, 0.0, result, 1)
      println("\nresult")
      result.foreach(println)
    }


    it("speed test") {
      val blas: BLAS = BLAS.getInstance

      val m = 1000000
      val n = 100
      val (a, b) = makeRandomMatrixAndVector(m, n)


      val matrix_A = DenseMatrix(a :_*)
      val vector_B = DenseMatrix(b :_*)
      val result1 = Timer.time{ matrix_A * vector_B }.toArray


      val result2 = Timer.time{ NetlibUtil.calcWIthJavaPrimitive(a, b) }


      val matrix_A2 = new Array[Double](m * n)
      for{
        (elm, i) <- a.zipWithIndex
        (score, j) <- elm.zipWithIndex
      }{matrix_A2(i + j * m) = score}
      val result3 = new Array[Double](m)
      Timer.time{ blas.dgemv("N", m, n, 1.0, matrix_A2, m, b, 1, 0.0, result3, 1) }

      println("\nresult1")
      result1.take(5).foreach(println)
      println("\nresult2")
      result2.take(5).foreach(println)
      println("\nresult3")
      result3.take(5).foreach(println)
    }
  }
}