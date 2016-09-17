name := """trackingSample"""

version := "1.0"

organization := "com.github.uryyyyyyy"
scalaVersion := "2.11.8"
libraryDependencies ++= Seq(
  "com.github.fommil.netlib" % "all" % "1.1.2",
  "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-natives" % "0.12",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

javaOptions in Test ++= Seq("-Dcom.github.fommil.netlib.BLAS=com.github.fommil.netlib.NativeSystemBLAS")