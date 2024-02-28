package com.vuj.analyzer.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ViewAnalyzerPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("viewAnalyzer", ViewAnalyzerExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(ViewAnalyzerClassVisitorFactory::class.java,
                InstrumentationScope.ALL) { params ->
                params.views.set(extension.views)
                params.logTag.set(extension.logTag)
                params.viewTag.set(extension.viewTag)
                params.behaviors.set(extension.behaviors)
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}