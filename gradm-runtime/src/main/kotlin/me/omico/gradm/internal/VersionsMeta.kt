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
package me.omico.gradm.internal

import me.omico.gradm.internal.path.GradmPaths
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

typealias VersionsMeta = Map<String, String>

fun VersionsMeta.store() {
    Files.write(
        GradmPaths.Metadata.versionsMeta,
        buildString { this@store.toSortedMap().forEach { (key, value) -> appendLine("$key=$value") } }.toByteArray(),
    )
    Files.write(
        GradmPaths.Metadata.versionsMetaHash,
        GradmPaths.Metadata.versionsMeta.sha1().toByteArray(),
    )
}

val localVersionsMeta: VersionsMeta
    get() = Files.readAllLines(GradmPaths.Metadata.versionsMeta)
        .associate {
            val strings = it.split("=")
            strings.first() to strings.last()
        }

val versionsMetaHash: String
    get() = Files.readString(GradmPaths.Metadata.versionsMetaHash)

private fun Path.sha1(): String = Files.readAllBytes(this).hash("SHA-1")

private fun ByteArray.hash(algorithm: String): String =
    MessageDigest
        .getInstance(algorithm)
        .digest(this)
        .fold("") { str, it -> str + "%02x".format(it) }
