import sbtrelease._
import ReleaseStateTransformations._

scalaVersion := "2.11.11"

name := "akka-scalapb-serialization"
organization := "im.actor"
organizationName := "Actor LLC"
organizationHomepage := Some(new URL("https://actor.im/"))

val akkaV = "2.5.2"

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
  "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.47" % "protobuf"
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

publishTo := {
  val nexus = "http://nexus.diegosilva.com.br:8081/nexus/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "content/repositories/releases")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.diegosilva.com.br", "admin", "admin123")

