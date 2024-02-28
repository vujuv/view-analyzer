package com.vuj.analyzer.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor

abstract class ViewAnalyzerClassVisitorFactory: AsmClassVisitorFactory<ViewAnalyzerParameters> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ViewAnalyzerClassVisitor(parameters.get(), classContext, nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val className = classData.className
        val views = parameters.orNull?.views?.orNull ?: return false
        return views.contains(className)
    }
}