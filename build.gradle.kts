plugins {
    id("java")
    `maven-publish`
}

group = "dev.tuhkanens.comfortlib"
version = "2.1.0"

repositories {
    mavenCentral()
}

dependencies {
    // ALL
    compileOnly("org.slf4j:slf4j-api:2.0.18")

    // MESSAGES
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:5.2.0")

    // MESSAGES AND CONFIG
    compileOnly("org.spongepowered:configurate-yaml:4.2.0")

    // UPDATE
    compileOnly("com.google.code.gson:gson:2.14.0")

    // DATABASE
    compileOnly("org.jdbi:jdbi3:3.0.0-beta2")
    // optional
    compileOnly("com.zaxxer:HikariCP:7.1.0")
    compileOnly("com.mysql:mysql-connector-j:9.7.0")
    compileOnly("org.xerial:sqlite-jdbc:3.53.2.0")
}

dependencies {

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}