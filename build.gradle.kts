import com.github.gradle.node.npm.task.NpmTask
import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.meta.jaxb.Property

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20"
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.2.0"
    id("io.micronaut.aot") version "4.2.0"
    id("nu.studer.jooq") version "8.2.1"
    id("com.github.node-gradle.node") version "7.0.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

version = "1.0"
group = "de.w3is"

val kotlinVersion = "1.9.20"
val micronautVersion = "4.1.6"
val postgresqlJdbcVersion = "42.6.0"
val jooqVersion = "3.18.7"
val nodeVersion = "21.2.0"
val nodeNpmVersion = "10.2.3"
val assertKVersion = "0.27.0"
val thumbnailatorVersion = "0.4.20"
val commonsTextVersion = "1.11.0"
val springSecurityVersion = "6.1.5"
val mockitoVersion = "5.1.0"
val h2Version = "2.2.224"

repositories {
    mavenCentral()
}

dependencies {
    jooqGenerator("org.jooq:jooq-meta-extensions:$jooqVersion")

    ksp("io.micronaut.serde:micronaut-serde-processor")

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("jakarta.transaction:jakarta.transaction-api")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.security:micronaut-security-annotations")

    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-jooq")

    implementation("io.projectreactor:reactor-core")
    implementation("org.springframework.security:spring-security-core:$springSecurityVersion")
    implementation("net.coobird:thumbnailator:$thumbnailatorVersion")
    implementation("org.apache.commons:commons-text:$commonsTextVersion")

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.h2database:h2:$h2Version")
    runtimeOnly("org.postgresql:postgresql:$postgresqlJdbcVersion")
    testImplementation("com.willowtreeapps.assertk:assertk:$assertKVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
}

kotlin {
    jvmToolchain(17)
}

node {
    download.set(true)
    version.set(nodeVersion)
    npmVersion.set(nodeNpmVersion)
    nodeProjectDir.set(file("${project.projectDir}/frontend/"))
}

application {
    mainClass.set("de.w3is.recipes.ApplicationKt")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }

    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }
}

graalvmNative.toolchainDetection.set(true)

micronaut {
    version(micronautVersion)
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("de.w3is.recipes.*")
    }
    aot {
        optimizeServiceLoading.set(true)
        convertYamlToJava.set(true)
        replaceLogbackXml.set(true)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
        possibleEnvironments.set(listOf("h2", "h2-local", "postgres"))
        targetEnvironments.set(listOf("h2", "postgres"))
    }
}

jooq {
    version.set(jooqVersion)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties = listOf(
                            Property().withKey("scripts").withValue("src/main/resources/db/migration/*.sql"),
                            Property().withKey("sort").withValue("flyway"),
                            Property().withKey("defaultNameCase").withValue("lower"),
                        )
                    }
                    target.apply {
                        packageName = "de.w3is.recipes.infra.persistence.generated"
                    }
                    generate.withJpaAnnotations(true)
                }
            }
        }
    }
}

ktlint {
    version.set("0.50.0")
    verbose.set(true)
    outputToConsole.set(true)
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

tasks.register<NpmTask>("buildFrontend") {
    dependsOn("npmInstall")
    args.addAll("run", "build")
    inputs.files("frontend/package.json", "frontend/package-lock.json")
    inputs.dir("frontend/src")
    inputs.dir(fileTree("frontend/node_modules").exclude(".cache"))
    outputs.dir("frontend/build")
}

tasks.register<Copy>("copyFrontendBundle") {
    dependsOn("buildFrontend")
    from(file("frontend/build/"))
    include("**/*")
    into("build/resources/main/public/")
}

tasks.named("processResources") {
    dependsOn("copyFrontendBundle")
}

tasks.named<JooqGenerate>("generateJooq") {
    inputs.files(fileTree("src/main/resources/db/migration"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    allInputsDeclared.set(true)
    outputs.cacheIf { true }
}
