package com.vuj.analyzer.plugin

import com.vuj.analyzer.plugin.behavior.ViewBehaviorRegistry
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter
class ViewAnalyzerAdviceAdapter(api: Int,
                                methodVisitor: MethodVisitor,
                                access: Int,
                                name: String?,
                                descriptor: String?,
                                private val registry: ViewBehaviorRegistry): AdviceAdapter(api, methodVisitor, access, name, descriptor) {
    override fun onMethodEnter() {
        name?.let {
            registry.get(name)?.onBehaviorBegin(name, mv)
        }
    }

    override fun onMethodExit(opcode: Int) {
        name?.let {
            registry.get(name)?.onBehaviorEnd(name, mv)
        }
    }
}