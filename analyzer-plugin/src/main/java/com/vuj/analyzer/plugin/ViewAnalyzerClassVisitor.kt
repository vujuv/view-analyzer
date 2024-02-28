package com.vuj.analyzer.plugin

import com.android.build.api.instrumentation.ClassContext
import com.vuj.analyzer.plugin.behavior.TouchEventBehaviorHook
import com.vuj.analyzer.plugin.behavior.ViewBehavior
import com.vuj.analyzer.plugin.behavior.ViewBehaviorRegistry
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ViewAnalyzerClassVisitor(
    parameters: ViewAnalyzerParameters,
    classContext: ClassContext,
    classVisitor: ClassVisitor): ClassVisitor(Opcodes.ASM5, classVisitor) {
    private val registry: ViewBehaviorRegistry = ViewBehaviorRegistry()

    init {
        parameters.behaviors.get().forEach {
            when (it) {
                ViewBehavior.TOUCH_EVENT -> registry.register(ViewBehavior.TOUCH_EVENT, TouchEventBehaviorHook(parameters, classContext, classVisitor))
                // 理论上不会走到这里
                else -> throw NullPointerException("can not contain null value in behaviors")
            }
        }

    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return ViewAnalyzerAdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor, registry)
    }
}