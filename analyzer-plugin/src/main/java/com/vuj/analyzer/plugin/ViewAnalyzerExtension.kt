package com.vuj.analyzer.plugin

import com.vuj.analyzer.plugin.behavior.ViewBehavior

open class ViewAnalyzerExtension {
    /**
     * 待分析行为的view全限定类名集合
     */
    var views: Set<String> = emptySet()

    /**
     * 日志标识
     */
    var logTag: String? = null

    /**
     * 待分析行为的view标识，可以通过View.setTag方法或者在xml中通过android:tag属性进行设置
     * 如果该字段为空，则表示所有在views集合中的view都打印日志
     */
    var viewTag: String? = null

    /**
     * 待分析的view行为集合
     */
    var behaviors: Set<ViewBehavior> = emptySet()
}