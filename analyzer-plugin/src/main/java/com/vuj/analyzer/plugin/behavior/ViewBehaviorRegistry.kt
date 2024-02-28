package com.vuj.analyzer.plugin.behavior

/**
 * view行为注册器，用于view行为和相应行为钩子之间的绑定
 */
class ViewBehaviorRegistry {
    private val hookMap: MutableMap<String, ViewBehaviorHook> = HashMap()

    fun register(viewBehavior: ViewBehavior, viewBehaviorHook: ViewBehaviorHook) {
        viewBehavior.methodSet.forEach {
            hookMap[it] = viewBehaviorHook
        }
    }

    fun get(methodName: String): ViewBehaviorHook? {
        return hookMap[methodName]
    }
}