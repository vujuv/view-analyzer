package com.vuj.analyzer.plugin

import com.android.build.api.instrumentation.InstrumentationParameters
import com.vuj.analyzer.plugin.behavior.ViewBehavior
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Internal

interface ViewAnalyzerParameters: InstrumentationParameters {
    @get:Internal
    val views: SetProperty<String>

    @get:Internal
    val logTag: Property<String>

    @get:Internal
    val viewTag: Property<String>

    @get:Internal
    val behaviors: SetProperty<ViewBehavior>
}