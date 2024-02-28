一个用于分析安卓view行为的插件，通过ASM字节码插桩技术在调用相应行为方法的时候增加相关日志，以此来分析view的行为
目前支持以下view行为：
1.TouchEvent，相应方法："onTouchEvent"、"onInterceptTouchEvent"
