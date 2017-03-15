import sbtrelease._
import ReleaseStateTransformations._

scalaVersion := "2.11.7"

name := "akka-scalapb-serialization"
organization := "im.actor"
organizationName := "Actor LLC"
organizationHomepage := Some(new URL("https://actor.im/"))

val akkaV = "2.4.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.47",
  "com.github.ben-manes.caffeine" % "caffeine" % "1.2.0"
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

// Make protos from some Jar available to import.
libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % "3.1.0" % "protobuf",
  "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.47"
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
