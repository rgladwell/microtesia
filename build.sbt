scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.11.12", "2.12.5", "2.13.1")

organization := "me.gladwell.microtesia"

organizationHomepage := Some(url("https://gladwell.me"))

licenses := Seq("LGPL-3.0" -> url("http://www.gnu.org/licenses/lgpl-3.0.html"))

homepage := Some(url("https://github.com/rgladwell/microtesia"))

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-Yrangepos"
)

libraryDependencies ++= Seq(
  "org.ccil.cowan.tagsoup" % "tagsoup"     % "1.2.1",
  "com.chuusai" %% "shapeless"             % "2.3.3",
  "org.scala-lang.modules" %% "scala-xml"  % "1.2.0",
  "org.specs2" %% "specs2-core"            % "4.6.0" % "test",
  "org.specs2" %% "specs2-scalacheck"      % "4.6.0" % "test",
  "me.gladwell.urimplicit" %% "urimplicit" % "0.3.0" % "test"
)

pomExtra := (
  <developers>
    <developer>
      <id>rgladwell</id>
      <name>Ricardo Gladwell</name>
      <url>https://gladwell.me</url>
    </developer>
  </developers>)

enablePlugins(GhpagesPlugin)

enablePlugins(SiteScaladocPlugin)

git.remoteRepo := "git@github.com:rgladwell/microtesia.git"

doctestDecodeHtmlEntities := true

doctestMarkdownEnabled := true
