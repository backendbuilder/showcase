plugins {
    `maven-publish`
    id("java-library")
    //id("org.springframework.boot") version "3.1.4"
    //id("io.spring.dependency-management") version "1.1.3"
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.showcase"
            artifactId = "shared-library"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
    repositories{
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/backendbuilder/showcase")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.token") as String?
            }
        }
    }

}

group = "com.showcase"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka_2.13:3.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0-rc1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}