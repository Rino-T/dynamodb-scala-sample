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
      "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.12.4"
    )
  )
