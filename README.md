# 简介

一个用于分析安卓view行为的插件，通过字节码插桩技术在view调用相应行为方法的时候增加相关日志，以此用来辅助分析相关view行为问题，例如：滑动冲突

目前支持以下view行为（后续准备扩展更多行为），一个view行为可能会对应多个行为方法：
1. 行为：TOUCH_EVENT，行为方法："onTouchEvent"、"onInterceptTouchEvent"

请注意：
1. 目前只支持将插件发布到本地使用，后续准备发布到远程
2. 目前只支持Java17，后续准备兼容Java8

应用：
```kotlin
plugins {
    id("com.vuj.view-analyzer-plugin")
}

viewAnalyzer {
    views = setOf("com.vuj.analyzer.CustomLinearLayout",
        "androidx.recyclerview.widget.RecyclerView") // view的全限定类名集合
    logTag = "vuj"  // 日志标识
    viewTag = "viewAnalyzer" // view标识，用以在运行时判断该view对象是否需要打印日志（同一个view类可能会对应多个view对象）
    behaviors = setOf(ViewBehavior.TOUCH_EVENT) // view行为集合
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.vuj.analyzer.CustomLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity"
    android:tag="viewAnalyzer">
    
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:tag="viewAnalyzer" />
</com.vuj.analyzer.CustomLinearLayout>
```

效果：
```text
vuj                     com.vuj.analyzer                     D  CustomLinearLayout-onInterceptTouchEvent-ACTION_DOWN
vuj                     com.vuj.analyzer                     D  RecyclerView-onInterceptTouchEvent-ACTION_DOWN
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_DOWN
vuj                     com.vuj.analyzer                     D  CustomLinearLayout-onInterceptTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  CustomLinearLayout-onInterceptTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_MOVE
vuj                     com.vuj.analyzer                     D  RecyclerView-onTouchEvent-ACTION_UP
```
