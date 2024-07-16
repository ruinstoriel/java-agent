package org.example.javaagent.asm;


import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;


import java.io.FileOutputStream;
import java.io.IOException;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.ClassReader;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Demo {

    public static void demo() {
        System.out.println("demo");
    }

    public static void main(String[] args) throws IOException {
        method();
    }
    public static void method() throws IOException {
        ClassReader reader = new ClassReader(Demo.class.getName());
        ClassNode node = new ClassNode(ASM9);
        reader.accept(node, 0);

        for (MethodNode mn : node.methods) {
            if ("demo".equals(mn.name) && "()V".equals(mn.desc)) {
                InsnList list = new InsnList();

                // 获取 System.out 字段
                list.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));

                // 将常量 "abc" 压入操作数栈
                list.add(new LdcInsnNode("aaa"));

                // 调用 PrintStream 的 println 方法
                list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));


                mn.instructions.insert(list);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        // 写回修改后的类字节码
        byte[] modifiedClassBytes = writer.toByteArray();
        String classFilePath = Demo.class.getResource("/").getPath()+"org/example/javaagent/asm/Demo.class";
        try (FileOutputStream fos = new FileOutputStream(classFilePath)) {
            fos.write(modifiedClassBytes);
        }

        System.out.println("Class modified successfully.");
    }

    public static void print() throws IOException {
        ClassReader classReader = new ClassReader(Demo.class.getName());
        ClassWriter classWriter = new ClassWriter(classReader,0);
        HelloWorldVisitor helloWorldVisitor = new HelloWorldVisitor(ASM9,classWriter);
        classReader.accept(helloWorldVisitor, 0);
        // 写回修改后的类字节码
        byte[] modifiedClassBytes = classWriter.toByteArray();
        String classFilePath = Demo.class.getResource("/").getPath()+"org/example/javaagent/asm/Demo.class";
        try (FileOutputStream fos = new FileOutputStream(classFilePath)) {
            fos.write(modifiedClassBytes);
        }

        System.out.println("Class modified successfully.");
    }
}
