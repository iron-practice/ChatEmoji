plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.6.0"
}

group = "org.ramki"
version = "1.0.0"

val paperApi = "1.21.11-R0.1-SNAPSHOT"
val mcVersion = paperApi.split("-")[0]
val cleanRun by tasks.registering(Delete::class) {
    delete(layout.projectDirectory.dir("run"))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperApi")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        dependsOn(cleanRun)
        minecraftVersion(mcVersion)
        runDirectory = rootDir.resolve("run/paper/$mcVersion")
        jvmArgs = listOf(
            "-Dcom.mojang.eula.agree=true",
            "-Xms2G",
            "-Xmx2G"
        )
        downloadPlugins {
            url("https://cdn.modrinth.com/data/Vebnzrzj/versions/b0mk8uS6/LuckPerms-Bukkit-5.5.71.jar?mr_download_reason=standalone&mr_game_version=1.21.11&mr_loader=paper")
        }
    }

    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}