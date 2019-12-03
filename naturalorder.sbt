import sbt._
import Settings._
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

lazy val root = project.root
  .setName("naturalorder")
  .setDescription("Build of naturalorder library")
  .configureRoot
  .noPublish
  .aggregate(naturalorderJVM, naturalorderJS)

lazy val naturalorder = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).build.from("naturalorder")
  .setName("naturalorder")
  .setDescription("Natural order sorting of Strings in Scala")
  .configureModule
  .publish
  .configureTests()
  .settings(libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core"       % Dependencies.specs2Version % "test",
    "org.specs2" %%% "specs2-scalacheck" % Dependencies.specs2Version % "test"
  ))

lazy val naturalorderJVM = naturalorder.jvm
lazy val naturalorderJS = naturalorder.js

lazy val readme = scalatex.ScalatexReadme(
  projectId = "readme",
  wd        = file(""),
  url       = "https://github.com/scalalandio/naturalorder/tree/master",
  source    = "Readme"
)
  .configureModule
  .noPublish
  .enablePlugins(GhpagesPlugin)
  .settings(
    siteSourceDirectory := target.value / "scalatex",
    git.remoteRepo := "git@github.com:scalalandio/naturalorder.git",
    Jekyll / makeSite / includeFilter := new FileFilter { def accept(p: File) = true }
  )

addCommandAlias("fullTest", ";test;scalastyle")
addCommandAlias("fullCoverageTest", ";coverage;test;coverageReport;coverageAggregate;scalastyle")
