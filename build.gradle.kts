plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    testCompile("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
