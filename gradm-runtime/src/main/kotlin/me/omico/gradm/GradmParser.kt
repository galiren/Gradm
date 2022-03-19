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
package me.omico.gradm

import me.omico.gradm.internal.asYamlDocument
import me.omico.gradm.internal.codegen.generateDependenciesProjectFiles
import me.omico.gradm.internal.config.gradmRuleVersion
import me.omico.gradm.internal.config.gradmVersion
import me.omico.gradm.internal.localVersionsMeta
import me.omico.gradm.internal.maven.MavenRepositoryMetadataParser
import me.omico.gradm.internal.path.RootProjectPaths

object GradmParser {

    fun execute(updateDependencies: Boolean = false) {
        val document = RootProjectPaths.gradmConfig.toFile().inputStream().asYamlDocument()
        println("Gradm version: ${document.gradmVersion}")
        println("Gradm rule version: ${document.gradmRuleVersion}")
        val versionsMeta = when {
            updateDependencies -> MavenRepositoryMetadataParser.updateVersionsMeta(document)
            else -> localVersionsMeta
        }
        generateDependenciesProjectFiles(document, versionsMeta)
    }
}
