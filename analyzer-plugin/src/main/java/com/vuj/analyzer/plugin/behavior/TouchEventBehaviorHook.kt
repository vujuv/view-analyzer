package com.vuj.analyzer.plugin.behavior

import com.android.build.api.instrumentation.ClassContext
import com.vuj.analyzer.plugin.ViewAnalyzerParameters
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
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

    override fun onBehaviorGenerate(name: String) {
        // 如果当前类不是ViewGroup的话，不需要生成"onInterceptTouchEvent"方法
        if (!classContext.currentClassData.superClasses.contains("android.view.ViewGroup")
            && name == "onInterceptTouchEvent") {
            return
        }

        val methodVisitor = classVisitor.visitMethod(
            Opcodes.ACC_PUBLIC,
            name,
            "(Landroid/view/MotionEvent;)Z",
            null,
            null
        )
        methodVisitor.visitCode()
        methodVisitor.visitLdcInsn(parameters.viewTag.get())
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/view/View",
            "getTag",
            "()Ljava/lang/Object;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/String",
            "equals",
            "(Ljava/lang/Object;)Z",
            false
        )
        val label0 = Label()
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label0)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(22, label1)
        methodVisitor.visitLdcInsn(parameters.logTag.get())
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/StringBuilder",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitLdcInsn("${getSimpleClassName(classContext.currentClassData.className)}-${name}-")
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/view/MotionEvent",
            "getAction",
            "()I",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/view/MotionEvent",
            "actionToString",
            "(I)Ljava/lang/String;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "d",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        methodVisitor.visitInsn(Opcodes.POP)
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(24, label0)
        methodVisitor.visitLineNumber(21, label0)
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "android/view/View",
            name,
            "(Landroid/view/MotionEvent;)Z",
            false
        )
        methodVisitor.visitInsn(Opcodes.IRETURN)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable(
            "this",
            "L${classContext.currentClassData.className};",
            null,
            label0,
            label2,
            0
        )
        methodVisitor.visitLocalVariable(
            "event",
            "Landroid/view/MotionEvent;",
            null,
            label0,
            label2,
            1
        )
        methodVisitor.visitMaxs(3, 2)
        methodVisitor.visitEnd()
    }

    private fun getSimpleClassName(fullClassName: String): String {
        return fullClassName.substringAfterLast('.')
    }
}