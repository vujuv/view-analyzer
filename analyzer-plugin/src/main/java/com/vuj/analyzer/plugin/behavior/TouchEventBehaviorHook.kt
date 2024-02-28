package com.vuj.analyzer.plugin.behavior

import com.android.build.api.instrumentation.ClassContext
import com.vuj.analyzer.plugin.ViewAnalyzerParameters
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * view触摸事件行为实现
 */
class TouchEventBehaviorHook(private val parameters: ViewAnalyzerParameters,
                             private val classContext: ClassContext,
                             private val classVisitor: ClassVisitor
): ViewBehaviorHook {
    override fun onBehaviorBegin(name: String, methodVisitor: MethodVisitor) {
        val currentClassName = classContext.currentClassData.className
        methodVisitor.visitLdcInsn(parameters.viewTag.get())
        methodVisitor.visitVarInsn(AdviceAdapter.ALOAD, 0)
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "android/view/View",
            "getTag",
            "()Ljava/lang/Object;",
            false
        )
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "java/lang/String",
            "equals",
            "(Ljava/lang/Object;)Z",
            false
        )
        val label1 = Label()
        methodVisitor.visitJumpInsn(AdviceAdapter.IFEQ, label1)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLineNumber(22, label2)
        methodVisitor.visitLdcInsn(parameters.logTag.get())
        methodVisitor.visitTypeInsn(AdviceAdapter.NEW, "java/lang/StringBuilder")
        methodVisitor.visitInsn(AdviceAdapter.DUP)
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKESPECIAL,
            "java/lang/StringBuilder",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitLdcInsn("${getSimpleClassName(currentClassName)}-${name}-")
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitVarInsn(AdviceAdapter.ALOAD, 1)
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "android/view/MotionEvent",
            "getAction",
            "()I",
            false
        )
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKESTATIC,
            "android/view/MotionEvent",
            "actionToString",
            "(I)Ljava/lang/String;",
            false
        )
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        methodVisitor.visitMethodInsn(
            AdviceAdapter.INVOKESTATIC,
            "android/util/Log",
            "d",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        methodVisitor.visitInsn(AdviceAdapter.POP)
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(24, label1)
        methodVisitor.visitFrame(AdviceAdapter.F_SAME, 0, null, 0, null)
    }

    override fun onBehaviorEnd(name: String, methodVisitor: MethodVisitor) {

    }

    private fun getSimpleClassName(fullClassName: String): String {
        return fullClassName.substringAfterLast('.')
    }
}