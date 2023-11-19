val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Carter",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "com.google.api-client" % "google-api-client" % "2.0.0",
      "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1",
      "com.google.apis" % "google-api-services-gmail" % "v1-rev20220404-2.0.0",
      "javax.mail" % "mail" % "1.4.7"
    )
  )
