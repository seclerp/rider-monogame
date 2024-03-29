@file:Suppress("HardCodedStringLiteral")

import org.jetbrains.changelog.exceptions.MissingVersionException
import java.util.*
import kotlin.collections.*

buildscript {
    repositories {
        maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
    }

    // https://search.maven.org/artifact/com.jetbrains.rd/rd-gen
    dependencies {
        classpath("com.jetbrains.rd:rd-gen:2023.3.2")
    }
}

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-repository/snapshots")
    maven("https://cache-redirector.jetbrains.com/maven-central")
}

plugins {
    id("me.filippov.gradle.jvm.wrapper") version "0.14.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.changelog
    id("org.jetbrains.changelog") version "2.2.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.intellij
    id("org.jetbrains.intellij") version "1.16.1"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    // https://plugins.gradle.org/plugin/org.jetbrains.grammarkit
    id("org.jetbrains.grammarkit") version "2022.3.1"
}

apply {
    plugin("com.jetbrains.rdgen")
}

dependencies {
    testImplementation("org.testng:testng:7.7.0")
}

val riderPluginId: String by project
val dotnetPluginId: String by project
val productVersion: String by project
val pluginVersion: String by project
val buildConfiguration = ext.properties["buildConfiguration"] ?: "Debug"

val publishToken: String by project
val publishChannel: String by project

val rdLibDirectory: () -> File = { file("${tasks.setupDependencies.get().idea.get().classes}/lib/rd") }
extra["rdLibDirectory"] = rdLibDirectory

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

apply(plugin = "com.jetbrains.rdgen")

configure<com.jetbrains.rd.generator.gradle.RdGenExtension> {
    val modelDir = file("$projectDir/protocol/src/main/kotlin/model")
    val csOutput = file("$projectDir/src/dotnet/$dotnetPluginId/Rd")
    val ktOutput = file("$projectDir/src/rider/main/kotlin/${riderPluginId.replace('.','/').lowercase(Locale.getDefault())}/rd")

    verbose = true
    classpath({
        "${rdLibDirectory()}/rider-model.jar"
    })
    sources("$modelDir/rider")
    hashFolder = "$buildDir"
    packages = "model.rider"

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        namespace = "me.seclerp.rider.plugins.efcore.model"
        directory = "$ktOutput"
    }

    generator {
        language = "csharp"
        transform = "reversed"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        namespace = "Rider.Plugins.EfCore"
        directory = "$csOutput"
    }
}

grammarKit {
    jflexRelease.set("1.9.1")
    grammarKitRelease.set("2022.3.1")
}

intellij {
    type.set("RD")
    version.set(productVersion)
    downloadSources.set(false)
    plugins.set(listOf<String>(
//        "com.intellij.database"
    ))
}

tasks {
    generateLexer {
        sourceFile.set(file("src/rider/main/kotlin/me/seclerp/rider/plugins/monogame/mgcb/Mgcb.flex"))
        targetDir.set("src/rider/gen/me/seclerp/rider/plugins/monogame/mgcb")
        targetClass.set("MgcbLexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        sourceFile.set(file("src/rider/main/kotlin/me/seclerp/rider/plugins/monogame/mgcb/Mgcb.bnf"))
        targetRoot.set("src/rider/gen")
        pathToParser.set("/parser/MgcbParser.java")
        pathToPsiRoot.set("/psi")
        purgeOldFiles.set(true)
    }

    wrapper {
        gradleVersion = "7.5.1"
        distributionType = Wrapper.DistributionType.ALL
        distributionUrl = "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
    }

    val riderSdkPath by lazy {
        val path = setupDependencies.get().idea.get().classes.resolve("lib/DotNetSdkForRdPlugins")
        if (!path.isDirectory) error("$path does not exist or not a directory")

        println("Rider SDK path: $path")
        return@lazy path
    }

    val generateNuGetConfig by registering {
        doLast {
            nuGetConfigFile.writeTextIfChanged("""
                <?xml version="1.0" encoding="utf-8"?>
                <!-- Auto-generated from 'generateNuGetConfig' task of old.build_gradle.kts -->
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
                ?.mapNotNull { file ->
                    val packageId = packageRefRegex.matchEntire(file.name)?.groupValues?.get(1) ?: return@mapNotNull null
                    val version = versionRegex.find(file.readText())?.groupValues?.get(1) ?: return@mapNotNull null

                    packageId to version
                }
                ?.filter { (packageId, _) -> !excludedNuGets.contains(packageId) } ?: emptyList()

            val directoryPackagesFileContents = buildString {
                appendLine("""
                    <!-- Auto-generated from 'generateSdkPackagesVersionsLock' task of old.build_gradle.kts -->
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

    val rdgen by existing

    register("prepare") {
        dependsOn(rdgen, generateLexer, generateParser, generateNuGetConfig, generateSdkPackagesVersionsLock)
    }

    val compileDotNet by registering {
        dependsOn(rdgen, generateNuGetConfig, generateSdkPackagesVersionsLock)
        doLast {
            exec {
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
                    exec {
                        workingDir(testsDir.absolutePath)
                        executable("dotnet")
                        args("test", "-c", buildConfiguration, it)
                    }
                }
        }
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn(rdgen, generateLexer, generateParser)
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    patchPluginXml {
        sinceBuild.set("233.0")
        untilBuild.set("233.*")
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

    withType<org.jetbrains.intellij.tasks.PrepareSandboxTask> {
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