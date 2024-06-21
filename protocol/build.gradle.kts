import com.jetbrains.rd.generator.gradle.RdGenTask

plugins {
    id("com.jetbrains.rdgen")
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://cache-redirector.jetbrains.com/maven-central")
}

val repoRoot: File = projectDir.parentFile

sourceSets {
    main {
        kotlin {
            srcDir(repoRoot.resolve("protocol/src/main/kotlin/model"))
        }
    }
}

rdgen {
    verbose = true
    packages = "model"

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        namespace = "com.jetbrains.rider.plugins.efcore.model"
        directory = file("$repoRoot/src/rider/main/kotlin/me/seclerp/rider/plugins/monogame/rd").absolutePath
    }

    generator {
        language = "csharp"
        transform = "reversed"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        namespace = "Rider.Plugins.EfCore"
        directory = file("$repoRoot/src/dotnet/Rider.Plugins.MonoGame/Rd").absolutePath
    }
}

tasks.withType<RdGenTask> {
    dependsOn(sourceSets["main"].runtimeClasspath)
    classpath(sourceSets["main"].runtimeClasspath)
}

dependencies {
    val rdVersion: String by project
    val rdKotlinVersion: String by project

    implementation("com.jetbrains.rd:rd-gen:$rdVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$rdKotlinVersion")
    implementation(
        project(
            mapOf(
                "path" to ":",
                "configuration" to "riderModel"
            )
        )
    )
}