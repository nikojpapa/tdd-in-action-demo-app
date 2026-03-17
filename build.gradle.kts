plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	groovy
}

group = "com.nvoulgaris"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.json:json:20250107")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "junit")
	}
	testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
	testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
	testImplementation("io.rest-assured:rest-assured:5.4.0")
	testImplementation("org.hamcrest:hamcrest:3.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

sourceSets {
	create("functional") {
		java.srcDirs("src/functional/groovy")
		resources.srcDir("src/functional/resources")
		compileClasspath += sourceSets["main"].output + configurations.testRuntimeClasspath.get()
		runtimeClasspath += output + compileClasspath
	}
}

configurations {
	getByName("functionalImplementation").extendsFrom(configurations.testImplementation.get())
	getByName("functionalRuntimeOnly").extendsFrom(configurations.testRuntimeOnly.get())
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests"
	group = "verification"
	testClassesDirs = sourceSets["functional"].output.classesDirs
	classpath = sourceSets["functional"].runtimeClasspath
	useJUnitPlatform()
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
        // This shows the actual assertion failure message
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = false
    }
}
