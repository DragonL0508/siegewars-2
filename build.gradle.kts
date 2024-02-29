import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    //Java plugin
    id("java-library")

    //Fairy framework plugin
    id("io.fairyproject") version "0.7.0b3-SNAPSHOT"

    // Dependency management plugin
    id("io.spring.dependency-management") version "1.1.0"

    //Kotlin plugin
    id("org.jetbrains.kotlin.jvm") version "1.9.22"

    //Shadow plugin, provides the ability to shade fairy and other dependencies to compiled jar
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = properties("group")
version = properties("version")

// Fairy configuration
fairy {
    name.set(properties("name"))
    // Main Package
    mainPackage.set(properties("package"))
    // Fairy Package
    fairyPackage.set(properties("package") + ".fairy")

    bukkitProperties().bukkitApi = "1.13"
}

runServer {
    version.set(properties("spigot.version"))
}

dependencies {
    api("io.fairyproject:bukkit-bundles")
    api("io.fairyproject:module.animation")
    api("io.fairyproject:bukkit-command")
    api("io.fairyproject:bukkit-menu")
    api("io.fairyproject:module.hologram")
    api("io.fairyproject:module.config")
    api("io.fairyproject:bukkit-xseries")
    api("io.fairyproject:bukkit-items")
    api("io.fairyproject:module.nametag")
    api("io.fairyproject:module.sidebar")
    api("io.fairyproject:bukkit-visibility")
    api("io.fairyproject:bukkit-visual")
    api("io.fairyproject:bukkit-timer")
    api("io.fairyproject:bukkit-nbt")
    api("io.fairyproject:module.tablist")
}

// Repositories
repositories {
    mavenCentral()
    maven(url = uri("https://oss.sonatype.org/content/repositories/snapshots/"))
    maven(url = uri("https://repo.codemc.io/repository/maven-public/"))
    // Spigot's repository for spigot api dependency
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
    maven(url = uri("https://repo.imanity.dev/imanity-libraries"))
}

// Dependencies
dependencies {
    // Spigot dependency
    compileOnly("org.spigotmc:spigot-api:${properties("spigot.version")}-R0.1-SNAPSHOT")
}

tasks.withType(ShadowJar::class.java) {
    // Relocate fairy to avoid plugin conflict
    relocate("io.fairyproject", "${properties("package")}.fairy")
    relocate("net.kyori", "${properties("package")}.fairy.libs.kyori")
    relocate("com.cryptomorin.xseries", "${properties("package")}.fairy.libs.xseries")
}