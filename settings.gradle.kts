@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Gradm"

pluginManagement {
    includeBuild("build-logic/gradm")
}

plugins {
    id("gradm")
    id("com.gradle.enterprise") version "3.13.3"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(!gradle.startParameter.isOffline)
    }
}

includeBuild("build-logic")

includeGradm(":gradm-codegen")
includeGradm(":gradm-gradle-plugin")
includeGradm(":gradm-integration")
includeGradm(":gradm-integration:api")
includeGradm(":gradm-integration:github")
includeGradm(":gradm-runtime")

include(":integration-testing")

fun includeGradm(path: String) {
    include(path)
    project(path).name = path.removePrefix(":").replace(":", "-")
}
