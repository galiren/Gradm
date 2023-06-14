import me.omico.consensus.dsl.requireRootProject

plugins {
    id("gradm")
    id("gradm.root.example-updater")
    id("gradm.root.git")
    id("gradm.root.gradle-enterprise-updater")
    id("gradm.root.spotless")
}

requireRootProject()

afterEvaluate {
    require(embeddedKotlinVersion == versions.kotlin) {
        "Embedded Kotlin version must be the same as the Kotlin version used by Gradm"
    }
}

val wrapper: Wrapper by tasks.named<Wrapper>("wrapper") {
    gradleVersion = versions.gradle
    distributionType = Wrapper.DistributionType.BIN
    finalizedBy(syncGradleWrapperForExamples)
}

val syncGradleWrapperForExamples by tasks.registering {
    file("examples").walk().maxDepth(1)
        .filter { directory -> directory.resolve("settings.gradle.kts").exists() }
        .map(File::getName)
        .forEach { example ->
            copy {
                from(wrapper.scriptFile, wrapper.batchScript)
                into("examples/$example")
            }
            copy {
                from(wrapper.jarFile, wrapper.propertiesFile)
                into("examples/$example/gradle/wrapper")
            }
        }
}
