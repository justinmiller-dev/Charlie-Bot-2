plugins {
    id("java")
}

group = "com.simplebot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.telegram:telegrambots-longpolling:9.2.0")
    implementation("org.telegram:telegrambots-client:9.2.0")
}

tasks.test {
    useJUnitPlatform()
}