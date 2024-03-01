package com.vuj.analyzer.plugin

import com.vuj.analyzer.plugin.behavior.ViewBehavior

open class ViewAnalyzerExtension {
    /**
     * view的全限定类名集合
     */
    var views: Set<String> = emptySet()

    /**
     * 日志标识
     */
    var logTag: String? = null

    /**
     * view标识，用以在运行时判断该view对象是否需要打印日志（同一个view类可能会对应多个view对象）
     * 可以利用view的setTag方法或者在xml中通过android:tag属性进行设置
     * 如果该配置项为空，则表示所有属于views集合中的view对象都打印日志
     */
    var viewTag: String? = null

    /**
     * view行为集合
     */
    var behaviors: Set<ViewBehavior> = emptySet()
}