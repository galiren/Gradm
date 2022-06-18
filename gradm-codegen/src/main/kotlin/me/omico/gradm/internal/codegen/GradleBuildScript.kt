/*
 * Copyright 2022 Omico
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.omico.gradm.internal.codegen

import me.omico.gradm.GRADM_VERSION
import me.omico.gradm.GradmDevelopmentConfigs
import me.omico.gradm.internal.path.GradmPaths
import kotlin.io.path.writeText

internal fun generateGradleBuildScript() {
    val content = GradmDevelopmentConfigs.customGradleBuildScript
        ?.ifBlank {
            println("Development: customGradleBuildScript is blank, fallback to default.")
            gradleBuildScriptContent
        }
        ?: gradleBuildScriptContent
    GradmPaths.GeneratedDependenciesProject.gradleBuildScript.writeText(content)
}

private val gradleBuildScriptContent: String =
    """
    //
    // Generated by Gradm, will be overwritten by every dependencies update, do not edit!!!
    //

    plugins {
        `embedded-kotlin`
    }

    kotlin {
        target.compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        compileOnly(gradleApi())
        compileOnly(gradleKotlinDsl())
        compileOnly(embeddedKotlin("gradle-plugin-api"))
        implementation("me.omico.gradm:gradm-runtime:$GRADM_VERSION")
    }

    """.trimIndent().replace("\n", System.lineSeparator())
