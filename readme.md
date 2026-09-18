# violetnotecore

Java 17 library (`com.romanpulov:violetnotecore`) with the VioletNote model, XML/PINS
processors and AES crypt services. Built with Gradle and published to the local Maven
repository for consumption by the VioletNote applications.

## Publish library to local repository
`./gradlew.ps1 clean publishToMavenLocal`

## Run the tests
`./gradlew.ps1 test`

## Toolchain

`gradlew.ps1` resolves the JDK itself through `Find-Java` from
`../common/builder/builder.psm1`, so `JAVA_HOME` no longer has to be set by hand.
Before that wrapper script existed, the build was prepared manually:

```
set JAVA_HOME=C:/WinApp/jdk-12.0.2/
set path=%PATH%;"D:/prj/apache-maven-3.3.9/bin"
```

## Recreating the Gradle wrapper

The wrapper under `gradle/wrapper/` is committed, so this is only needed when moving to a
new Gradle version. Run a standalone Gradle installation once:

```
<gradle-home>\bin\gradle wrapper
```

(originally generated with `gradle-5.6`)
