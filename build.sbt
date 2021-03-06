lazy val commonSettings = commonSmlBuildSettings ++ Seq(
  organization := "com.softwaremill.zioenv",
  scalaVersion := "2.13.2"
)

lazy val rootProject = (project in file("."))
  .settings(commonSettings: _*)
  .settings(publishArtifact := false, name := "zioenv")
  .aggregate(core)

lazy val core: Project = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC20"
    )
  )

