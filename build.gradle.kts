@file:Suppress("HardCodedStringLiteral")

import com.jetbrains.plugin.structure.base.utils.isFile
import com.jetbrains.plugin.structure.base.utils.listFiles
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.changelog.exceptions.MissingVersionException
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import kotlin.collections.*
import kotlin.io.path.absolute
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readText

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://cache-redirector.jetbrains.com/intellij-repository/releases")
    maven("https://cache-redirector.jetbrains.com/intellij-repository/snapshots")
    maven("https://cache-redirector.jetbrains.com/maven-central")
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

val grammarKitMissingDependencies by configurations.creating

plugins {
    id("me.filippov.gradle.jvm.wrapper")
    // https://plugins.gradle.org/plugin/org.jetbrains.changelog
    id("org.jetbrains.changelog") version "2.5.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.intellij.platform
    id("org.jetbrains.intellij.platform")
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    id("org.jetbrains.kotlin.jvm")
    // https://plugins.gradle.org/plugin/org.jetbrains.grammarkit
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

jvmWrapper {
    linuxAarch64JvmUrl = "https://download.oracle.com/java/21/archive/jdk-21.0.3_linux-aarch64_bin.tar.gz"
    linuxX64JvmUrl = "https://download.oracle.com/java/21/archive/jdk-21.0.3_linux-x64_bin.tar.gz"
    macAarch64JvmUrl = "https://download.oracle.com/java/21/archive/jdk-21.0.3_macos-aarch64_bin.tar.gz"
    macX64JvmUrl = "https://download.oracle.com/java/21/archive/jdk-21.0.3_macos-x64_bin.tar.gz"
    windowsX64JvmUrl = "https://download.oracle.com/java/21/archive/jdk-21.0.3_windows-x64_bin.zip"
}

dependencies {
    testImplementation("org.testng:testng:7.11.0")
}

val riderPluginId: String by project
val dotnetPluginId: String by project
val productVersion: String by project
val pluginVersion: String by project
val buildConfiguration = ext.properties["buildConfiguration"] ?: "Debug"

intellijPlatform {
    buildSearchableOptions = buildConfiguration == "Release"
}

val publishToken: String by project
val publishChannel: String by project

val dotNetSrcDir = File(projectDir, "src/dotnet")

val nuGetSdkPackagesVersionsFile = File(dotNetSrcDir, "RiderSdk.PackageVersions.Generated.props")
val nuGetConfigFile = File(dotNetSrcDir, "nuget.config")

version = pluginVersion

fun File.writeTextIfChanged(content: String) {
    val bytes = content.toByteArray()

    if (!exists() || !readBytes().contentEquals(bytes)) {
        println("Writing $path")
        parentFile.mkdirs()
        writeBytes(bytes)
    }
}

repositories {
    maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
}

sourceSets {
    main {
        kotlin.srcDir("src/rider/main/kotlin")
        resources.srcDir("src/rider/main/resources")
        java.srcDir("src/rider/gen")
    }
}

dependencies {
    intellijPlatform {
        rider(productVersion) {
            useInstaller = false
        }
        bundledModule("intellij.rider")
        jetbrainsRuntime()
    }

    // Workaround for https://youtrack.jetbrains.com/issue/IJPL-217565/it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap-in-253.x
    grammarKitMissingDependencies("it.unimi.dsi:fastutil:8.5.18")
    grammarKitMissingDependencies("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
    grammarKitMissingDependencies("org.jetbrains.intellij.deps:asm-all:9.6.1")
}

grammarKit {
    jflexRelease.set("1.9.1")
    grammarKitRelease.set("2022.3.1")
}

val riderModel: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(riderModel.name, provider {
        intellijPlatform.platformPath.resolve("lib/rd/rider-model.jar").also {
            check(it.isFile) {
                "rider-model.jar is not found at $it"
            }
        }
    }) {
        builtBy(Constants.Tasks.INITIALIZE_INTELLIJ_PLATFORM_PLUGIN)
    }
}

tasks {
    generateLexer {
        sourceFile.set(file("src/rider/main/kotlin/me/seclerp/rider/plugins/monogame/mgcb/Mgcb.flex"))
        targetOutputDir.set(file("src/rider/gen/me/seclerp/rider/plugins/monogame/mgcb"))
        purgeOldFiles.set(true)
    }

    generateParser {
        classpath += files(grammarKitMissingDependencies)

        sourceFile.set(file("src/rider/main/kotlin/me/seclerp/rider/plugins/monogame/mgcb/Mgcb.bnf"))
        targetRootOutputDir.set(file("src/rider/gen"))
        pathToParser.set("/parser/MgcbParser.java")
        pathToPsiRoot.set("/psi")
        purgeOldFiles.set(true)
    }

    wrapper {
        gradleVersion = "9.2.1"
        distributionType = Wrapper.DistributionType.ALL
        distributionUrl = "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
    }

    val riderSdkPath by lazy {
        val path = intellijPlatform.platformPath.resolve("lib/DotNetSdkForRdPlugins").absolute()
        if (!path.isDirectory()) error("$path does not exist or not a directory")

        println("Rider SDK path: $path")
        return@lazy path
    }

    val generateNuGetConfig by registering {
        doLast {
            nuGetConfigFile.writeTextIfChanged("""
                <?xml version="1.0" encoding="utf-8"?>
                <!-- Auto-generated from 'generateNuGetConfig' task of build.gradle.kts -->
                <!-- Run `gradlew :prepare` to regenerate -->
                <configuration>
                  <packageSources>
                    <add key="rider-sdk" value="$riderSdkPath" />
                  </packageSources>
                </configuration>
            """.trimIndent())
        }
    }

    val generateSdkPackagesVersionsLock by registering {
        doLast {
            val excludedNuGets = setOf(
                "NETStandard.Library"
            )
            val sdkPropsFolder = riderSdkPath.resolve("Build")
            val packageRefRegex = "PackageReference\\.(.+).Props".toRegex()
            val versionRegex = "<Version>(.+)</Version>".toRegex()
            val packagesWithVersions = sdkPropsFolder.listFiles()
                .mapNotNull { file ->
                    val packageId = packageRefRegex.matchEntire(file.name)?.groupValues?.get(1) ?: return@mapNotNull null
                    val version = versionRegex.find(file.readText())?.groupValues?.get(1) ?: return@mapNotNull null

                    packageId to version
                }
                .filter { (packageId, _) -> !excludedNuGets.contains(packageId) } ?: emptyList()

            val directoryPackagesFileContents = buildString {
                appendLine("""
                    <!-- Auto-generated from 'generateSdkPackagesVersionsLock' task of build_gradle.kts -->
                    <!-- Run `gradlew :prepare` to regenerate -->
                    <Project>
                      <ItemGroup>
                """.trimIndent())
                for ((packageId, version) in packagesWithVersions) {
                    appendLine(
                        "    <PackageVersion Include=\"${packageId}\" Version=\"${version}\" />"
                    )
                }
                appendLine("""
                    </ItemGroup>
                  </Project>
                """.trimIndent())
            }

            nuGetSdkPackagesVersionsFile.writeTextIfChanged(directoryPackagesFileContents)
        }
    }

    register("prepare") {
        dependsOn(":protocol:rdgen", generateLexer, generateParser, generateNuGetConfig, generateSdkPackagesVersionsLock)
    }

    val compileDotNet by registering {
        dependsOn(":protocol:rdgen", generateNuGetConfig, generateSdkPackagesVersionsLock)
        doLast {
            serviceOf<ExecOperations>().exec {
                workingDir(dotNetSrcDir)
                executable("dotnet")
                args("build", "-c", buildConfiguration)
            }
        }
    }

    register("testDotNet") {
        dependsOn(compileDotNet)
        doLast {
            val testsDir = dotNetSrcDir.resolve("Tests")
            testsDir.list { entry, name -> entry.isDirectory && name != ".DS_Store" }
                ?.forEach {
                    serviceOf<ExecOperations>().exec {
                        workingDir(testsDir.absolutePath)
                        executable("dotnet")
                        args("test", "-c", buildConfiguration, it)
                    }
                }
        }
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn(":protocol:rdgen", generateLexer, generateParser)
        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_21
                freeCompilerArgs.add("-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }

    patchPluginXml {
        sinceBuild.set("253.0")
        val latestChangelog = try {
            changelog.getUnreleased()
        } catch (_: MissingVersionException) {
            changelog.getLatest()
        }
        changeNotes.set(provider {
            changelog.renderItem(
                latestChangelog
                    .withHeader(false)
                    .withEmptySections(false),
                org.jetbrains.changelog.Changelog.OutputType.HTML
            )
        })
    }

    buildPlugin {
        dependsOn(compileDotNet)

        copy {
            from("${buildDir}/distributions/${rootProject.name}-${version}.zip")
            into("${rootDir}/output")
        }
    }

    runIde {
        // For statistics:
        // jvmArgs("-Xmx1500m", "-Didea.is.internal=true", "-Dfus.internal.test.mode=true")
        jvmArgs("-Xmx1500m")
    }

    test {
        useTestNG()
        testLogging {
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        environment["LOCAL_ENV_RUN"] = "true"
    }

    withType<PrepareSandboxTask> {
        dependsOn(compileDotNet)

        val outputFolder = file("$dotNetSrcDir/$dotnetPluginId/bin/$dotnetPluginId/$buildConfiguration")
        val backendFiles = listOf(
            "$outputFolder/$dotnetPluginId.dll",
            "$outputFolder/$dotnetPluginId.pdb"

            // TODO: add additional assemblies
        )

        for (f in backendFiles) {
            from(f) { into("${rootProject.name}/dotnet") }
        }

        doLast {
            for (f in backendFiles) {
                val file = file(f)
                if (!file.exists()) throw RuntimeException("File \"$file\" does not exist")
            }
        }
    }

    publishPlugin {
        token.set(publishToken)
        channels.set(listOf(publishChannel))
    }
}