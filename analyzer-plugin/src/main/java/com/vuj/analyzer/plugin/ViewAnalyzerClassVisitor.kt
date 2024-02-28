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
    // 待生成方法的集合
    private val generateMethodSet: MutableSet<String>

    init {
        parameters.behaviors.get().forEach {
            when (it) {
                ViewBehavior.TOUCH_EVENT -> registry.register(ViewBehavior.TOUCH_EVENT, TouchEventBehaviorHook(parameters, classContext, classVisitor))
                // 理论上不会走到这里
                else -> throw NullPointerException("can not contain null value in behaviors")
            }
        }
        generateMethodSet = registry.getMethodSet().toMutableSet()
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        // 如果类中有此方法的话，则在待生成方法集合中删除
        if (generateMethodSet.contains(name)) {
            generateMethodSet.remove(name)
        }
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return ViewAnalyzerAdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor, registry)
    }

    override fun visitEnd() {
        // 没有需要生成的方法
        if (generateMethodSet.isEmpty()) {
            return
        }
        // 根据待生成方法集合中的内容生成相应的方法
        generateMethodSet.forEach {
            registry.get(it)?.onBehaviorGenerate(it)
        }
    }
}