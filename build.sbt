scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", "2.11.7")

organization := "me.gladwell.microtesia"

organizationHomepage := Some(url("https://gladwell.me"))

licenses := Seq("LGPL-3.0" -> url("http://www.gnu.org/licenses/lgpl-3.0.html"))

homepage := Some(url("https://github.com/rgladwell/microtesia"))

scalacOptions ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-Yrangepos"
)

libraryDependencies ++= Seq(
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.4" % "test",
  "me.gladwell.urimplicit" %% "urimplicit" % "0.1" % "test"
)

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if (scalaMajor >= 11) =>
      libraryDependencies.value ++ Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.5")
    case _ => libraryDependencies.value
  }
}

pomExtra := (
  <scm>
    <url>git@github.com:rgladwell/microtesia.git</url>
    <connection>scm:git:git@github.com:rgladwell/microtesia.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rgladwell</id>
      <name>Ricardo Gladwell</name>
      <url>https://gladwell.me</url>
    </developer>
  </developers>)
