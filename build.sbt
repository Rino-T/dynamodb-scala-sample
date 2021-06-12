lazy val root = (project in file("."))
  .settings(
    name := "DynamodbScalaSample",
    version := "0.1",
    scalaVersion := "2.13.6",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation"
    ),
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "dynamodb" % "2.16.83"
    )
  )
