package com.vuj.analyzer.plugin.behavior

enum class ViewBehavior(val methodSet: Set<String>) {
    TOUCH_EVENT(setOf("onTouchEvent", "onInterceptTouchEvent"))
}