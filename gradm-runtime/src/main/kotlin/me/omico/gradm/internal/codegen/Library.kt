package me.omico.gradm.internal.codegen

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import me.omico.gradm.internal.VersionsMeta
import me.omico.gradm.internal.config.Library
import me.omico.gradm.internal.config.alias
import java.util.Locale

internal data class CodegenLibrary(
    val module: String,
    val alias: String,
    val version: String,
)

internal fun Library.toCodegenLibrary(versionsMeta: VersionsMeta): CodegenLibrary =
    CodegenLibrary(
        module = module,
        alias = alias(),
        version = version ?: versionsMeta[module]!!,
    )

internal fun TypeSpec.Builder.addLibrary(library: CodegenLibrary): TypeSpec.Builder =
    apply {
        PropertySpec.builder(library.alias.camelCase(), String::class)
            .initializer("${library.alias.camelCase()}(\"${library.version}\")")
            .build()
            .also(::addProperty)
        FunSpec.builder(library.alias.camelCase())
            .addParameter("version", String::class)
            .returns(String::class)
            .addStatement("return \"${library.module}:\$version\"", String::class)
            .build()
            .also(::addFunction)
    }

private fun String.camelCase() =
    split("-", "_")
        .mapIndexed { index, s ->
            if (index == 0) s
            else s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
        .joinToString("")
