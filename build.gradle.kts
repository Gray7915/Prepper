plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.prepper")
    mainClass.set("com.prepper.HelloApplication")
}

javafx {
    version = "23.0.2" // latest JavaFX 23 release
    modules = listOf("javafx.controls", "javafx.fxml")
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    // SQLite JDBC driver
    implementation("org.xerial:sqlite-jdbc:3.42.0.0") // latest stable

    // JDBI 3 core and SQL object support
    implementation("org.jdbi:jdbi3-core:3.42.0")
    implementation("org.jdbi:jdbi3-sqlobject:3.42.0")

    implementation("org.slf4j:slf4j-simple:2.0.9")
    //Charts and graphs hooray
    implementation("eu.hansolo:tilesfx:21.0.9")
}
tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
