plugins {
    kotlin("jvm") version "1.5.10"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    val log4jVersion = "1.2.14"
    implementation("log4j:log4j:$log4jVersion")

    implementation("com.sparkjava:spark-core:2.9.3")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}