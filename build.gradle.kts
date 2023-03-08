import com.github.rahulsom.waena.WaenaExtension
import nebula.plugin.contacts.Contact

plugins {
  id("org.jetbrains.kotlin.jvm").version("1.8.10")
  id("com.github.rahulsom.waena.root").version("0.6.1")
  id("com.github.rahulsom.waena.published").version("0.6.1")
  id("application")
  id("com.google.cloud.tools.jib").version("3.3.1")
}

allprojects {
  group = "com.github.rahulsom"
}

description = "Open Rewrite CLI"

contacts {
  validateEmails = true
  addPerson("rahul.som@gmail.com", closureOf<Contact> {
    moniker("Rahul Somasunderam")
    roles("owner")
    github("https://github.com/rahulsom")
  })
}

waena {
  license.set(WaenaExtension.License.Apache2)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.openrewrite.recipe:rewrite-recipe-bom:1.16.3"))
  implementation("org.openrewrite.recipe:rewrite-java-dependencies")
  implementation("org.openrewrite.recipe:rewrite-java-security")
  implementation("org.openrewrite.recipe:rewrite-kubernetes")
  implementation("org.openrewrite.recipe:rewrite-logging-frameworks")
  implementation("org.openrewrite.recipe:rewrite-migrate-java")
  implementation("org.openrewrite.recipe:rewrite-spring")
  implementation("org.openrewrite.recipe:rewrite-testing-frameworks")
  implementation("org.openrewrite:rewrite-gradle")
  implementation("org.openrewrite:rewrite-groovy")
  implementation("org.openrewrite:rewrite-java")
  implementation("org.openrewrite:rewrite-java-17")
  implementation("org.openrewrite:rewrite-json")
  implementation("org.openrewrite:rewrite-kotlin")
  implementation("org.openrewrite:rewrite-properties")
  implementation("org.openrewrite:rewrite-xml")
  implementation("org.openrewrite:rewrite-yaml")

  implementation("info.picocli:picocli:4.7.1")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
  testImplementation("org.assertj:assertj-core:3.24.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

}

application {
  mainClass.set("com.github.rahulsom.orc.ApplicationKt")
}

jib {
  from {
    image = "eclipse-temurin:17-jdk"
    platforms {
      platform {
        architecture = "amd64"
        os = "linux"
      }
      platform {
        architecture = "arm"
        os = "linux"
      }
      platform {
        architecture = "arm64"
        os = "linux"
      }
    }
  }
  to {
    image = "rahulsom/orc"
    tags =
      when {
        project.version.toString().contains("-dev") -> setOf("unstable")
        project.version.toString().contains("-SNAPSHOT") -> setOf("unstable")
        else -> setOf("stable", "latest", project.version.toString())
      }
    auth {
      username = System.getenv("DOCKER_USERNAME")
      password = System.getenv("DOCKER_PASSWORD")
    }
  }
}

tasks.withType<Test>() {
  useJUnitPlatform()
}

tasks.getByName("final").dependsOn("jib")
tasks.getByName("snapshot").dependsOn("jib")
