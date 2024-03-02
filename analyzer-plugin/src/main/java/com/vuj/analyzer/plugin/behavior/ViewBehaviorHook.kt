package com.vuj.analyzer.plugin.behavior

import org.objectweb.asm.commons.AdviceAdapter

/**
 * view行为钩子，对view行为方法进行一些定制化动作
 */
interface ViewBehaviorHook {
    fun onMethodEnter(adapter: AdviceAdapter)
    fun onMethodExit(opcode: Int, adapter: AdviceAdapter)
    fun onMethodGenerate(name: String)
}