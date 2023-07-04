buildscript {
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}

plugins {
    `kotlin-dsl`
    id("me.omico.gradm") version "3.3.2"
}

repositories {
    mavenCentral()
}

gradm {
    pluginId = "gradm"
    debug = true
    experimental {
        kotlinMultiplatformSupport = true
        kotlinMultiplatformIgnoredExtensions = listOf("androidx")
    }
}
