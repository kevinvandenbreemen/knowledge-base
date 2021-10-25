plugins {
    kotlin("jvm") version "1.5.10"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    val log4jVersion = "1.2.14"
    implementation("log4j:log4j:$log4jVersion")

    implementation("com.sparkjava:spark-core:2.9.3")

    val sqliteDAOVersion = "1.1.0.2000"
    implementation("com.github.kevinvandenbreemen:sqlite-dao:$sqliteDAOVersion")

    testImplementation("org.amshove.kluent:kluent:1.68")

    val markdownLibVersion = "0.18.0"
    implementation("org.commonmark:commonmark:$markdownLibVersion")

}

val fatJar = task("FatJar", type = Jar::class) {

    val jarName = "knowledgeBase.jar"

    archiveName = jarName
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes["Main-Class"] = "com.nvandenbreemen.kb.server.MainServer"

    }
    from(configurations.runtimeClasspath.get().map {
        if(it.isDirectory) it else zipTree(it)
    })
    with(tasks.jar.get() as CopySpec)

    copy {
        from("build/libs/$jarName")
        into("./")
    }
    println("Built and copied $jarName")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}