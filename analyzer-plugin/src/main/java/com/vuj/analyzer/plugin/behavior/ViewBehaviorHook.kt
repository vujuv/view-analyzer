package com.vuj.analyzer.plugin.behavior

import org.objectweb.asm.MethodVisitor

/**
 * view行为钩子，对view行为进行一些定制化动作
 */
interface ViewBehaviorHook {
    fun onBehaviorBegin(name: String, methodVisitor: MethodVisitor)
    fun onBehaviorEnd(name: String, methodVisitor: MethodVisitor)
    fun onBehaviorGenerate(name: String)
}