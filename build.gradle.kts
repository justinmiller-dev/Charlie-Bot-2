plugins {
    id("java")
}

group = "com.simplebot"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.telegram:telegrambots-longpolling:9.2.0")
    implementation("org.telegram:telegrambots-client:9.2.0")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:9.5.0")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    manifest {
        attributes("Main-Class" to "com.simplebot.Main")
    }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        configurations.runtimeClasspath.get().forEach { file ->
            from(zipTree(file.absoluteFile)){
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
    }
}



