plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.opencsv:opencsv:5.6")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jfree:jfreechart:1.0.19")
}

tasks.test {
    useJUnitPlatform()
}