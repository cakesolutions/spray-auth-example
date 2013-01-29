/** Project */
name := "auth"

version := "0.1"

organization := "org.cakesolutions"

scalaVersion := "2.10.0"

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

/** Dependencies */
resolvers += "snapshots-repo" at "http://scala-tools.org/repo-snapshots"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies <<= scalaVersion { scala_version => 
  val sprayVersion = "1.1-M7"
  val akkaVersion  = "2.1.0"
  val scalazVersion = "7.0.0-M7"
  Seq(
    "com.typesafe.akka"     % "akka-kernel_2.10"                % akkaVersion,
    "com.typesafe.akka"     % "akka-actor_2.10"                 % akkaVersion,
    "com.typesafe.akka"     % "akka-contrib_2.10"               % akkaVersion,
    "com.typesafe.akka"     % "akka-slf4j_2.10"                 % akkaVersion,
    "com.typesafe.akka"     % "akka-testkit_2.10"               % akkaVersion  % "test",
    "io.spray"              % "spray-can"                       % sprayVersion,
    "io.spray"              % "spray-routing"                   % sprayVersion,
    "io.spray"              % "spray-httpx"                     % sprayVersion,
    "io.spray"              % "spray-util"                      % sprayVersion,
    "io.spray"              % "spray-client"                    % sprayVersion,
    "io.spray"              % "spray-json_2.10"                      % "1.2.3",
    "io.spray"              % "spray-testkit"                   % sprayVersion % "test",
    "com.typesafe"          % "slick_2.10"                      % "1.0.0-RC1",
    "com.h2database"        % "h2"                              % "1.3.166",
    "org.scalaz"            % "scalaz-core_2.10"                % scalazVersion,
    "org.specs2"            % "specs2_2.10"                          % "1.13" % "test"
  )
}

/** Compilation */
javaOptions += "-Xmx2G"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

maxErrors := 20 

pollInterval := 1000

logBuffered := false

cancelable := true

/**This avoids weird race conditions during tests execution **/
parallelExecution in Test := false

testOptions := Seq(Tests.Filter(s =>
  Seq("Spec", "Suite", "Test", "Unit", "all").exists(s.endsWith(_)) &&
    !s.endsWith("FeaturesSpec") ||
    s.contains("UserGuide") || 
    s.contains("index") ||
    s.matches("org.specs2.guide.*")))

/** Console */
initialCommands in console := "import org.cakesolutions.auth._"
