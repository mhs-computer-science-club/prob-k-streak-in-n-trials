plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    implementation("commons-cli:commons-cli:1.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

/**
 * Task to make jar built with all dependencies embedded.
 */
val fatJar = task("fatJar", type = Jar::class) {
    manifest {
        attributes["Main-Class"] = "ProbKStreakInNTosses"
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    with(tasks["jar"] as CopySpec)
}

tasks.named<Task>("build") {
    dependsOn(fatJar)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
