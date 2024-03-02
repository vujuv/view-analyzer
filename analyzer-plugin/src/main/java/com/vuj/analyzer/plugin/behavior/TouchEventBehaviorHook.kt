package com.vuj.analyzer.plugin.behavior

import com.android.build.api.instrumentation.ClassContext
import com.vuj.analyzer.plugin.ViewAnalyzerParameters
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

class TouchEventBehaviorHook(private val parameters: ViewAnalyzerParameters,
                             private val classContext: ClassContext,
                             private val classVisitor: ClassVisitor
): ViewBehaviorHook {
    override fun onMethodEnter(adapter: AdviceAdapter) {

    }

    override fun onMethodExit(opcode: Int, adapter: AdviceAdapter) {
        // 对于Touch_Event行为而言，行为方法返回值类型必为boolean，这里无需再通过opcode判断了
        val currentClassName = classContext.currentClassData.className

        val local = adapter.newLocal(Type.BOOLEAN_TYPE)
        adapter.storeLocal(local)
        adapter.visitLdcInsn(parameters.viewTag.get())
        adapter.visitVarInsn(Opcodes.ALOAD, 0)
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            transDescriptorClassName(currentClassName),
            "getTag",
            "()Ljava/lang/Object;",
            false
        )
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/String",
            "equals",
            "(Ljava/lang/Object;)Z",
            false
        )
        val label2 = adapter.newLabel()
        adapter.visitJumpInsn(Opcodes.IFEQ, label2)
        val label3 = adapter.newLabel()
        adapter.visitLabel(label3)
        adapter.visitLdcInsn(parameters.logTag.get())
        adapter.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder")
        adapter.dup()
        adapter.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/StringBuilder",
            "<init>",
            "()V",
            false
        )
        adapter.visitLdcInsn("${getSimpleClassName(currentClassName)}-${adapter.name}-")
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        adapter.visitVarInsn(Opcodes.ALOAD, 1)
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/view/MotionEvent",
            "getAction",
            "()I",
            false
        )
        adapter.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/view/MotionEvent",
            "actionToString",
            "(I)Ljava/lang/String;",
            false
        )
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        adapter.visitLdcInsn("-")
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        adapter.loadLocal(local)
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Z)Ljava/lang/StringBuilder;",
            false
        )
        adapter.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        adapter.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "d",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        adapter.pop()
        adapter.visitLabel(label2)
        adapter.loadLocal(local)
    }

    override fun onMethodGenerate(name: String) {
        // 如果当前类不是ViewGroup的话，不需要生成"onInterceptTouchEvent"方法
        if (!classContext.currentClassData.superClasses.contains("android.view.ViewGroup")
            && name == "onInterceptTouchEvent") {
            return
        }

        val currentClassName = classContext.currentClassData.className
        val parentClassName = classContext.currentClassData.superClasses[0]

        val methodVisitor = classVisitor.visitMethod(
            Opcodes.ACC_PUBLIC,
            name,
            "(Landroid/view/MotionEvent;)Z",
            null,
            null
        )
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(
            Opcodes. INVOKESPECIAL,
            transDescriptorClassName(parentClassName),
            name,
            "(Landroid/view/MotionEvent;)Z",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ISTORE, 2)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLdcInsn(parameters.viewTag.get())
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            transDescriptorClassName(currentClassName),
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
        val label2 = Label()
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label2)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
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
        methodVisitor.visitLdcInsn("${getSimpleClassName(currentClassName)}-${name}-")
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
        methodVisitor.visitLdcInsn("-")
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 2)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Z)Ljava/lang/StringBuilder;",
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
        methodVisitor.visitLabel(label2)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 1, arrayOf<Any>(Opcodes.INTEGER), 0, null)
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 2)
        methodVisitor.visitInsn(Opcodes.IRETURN)
        val label4 = Label()
        methodVisitor.visitLabel(label4)
        methodVisitor.visitLocalVariable(
            "this",
            "L${transDescriptorClassName(currentClassName)};",
            null,
            label0,
            label4,
            0
        )
        methodVisitor.visitLocalVariable(
            "event",
            "Landroid/view/MotionEvent;",
            null,
            label0,
            label4,
            1
        )
        methodVisitor.visitLocalVariable("handled", "Z", null, label1, label4, 2)
        methodVisitor.visitMaxs(3, 3)
        methodVisitor.visitEnd()
    }

    private fun getSimpleClassName(fullClassName: String): String {
        return fullClassName.substringAfterLast('.')
    }

    /**
     * 转换为描述符类名形式，如：android.view.MotionEvent -> android/view/MotionEvent
     */
    private fun transDescriptorClassName(className: String): String {
        return className.replace('.', '/')
    }
}