package org.example.javaagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        // 注册一个 ClassFileTransformer
        System.out.println("premain 执行了");
        inst.addTransformer(new MyClassFileTransformer(), true);
    }

    static class MyClassFileTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            // 修改字节码的逻辑
            System.out.println("----"+className);
            if (className.equals("org/example/DemoController")) {
                System.out.println("demo");
            }
            return classfileBuffer;
        }
    }

    public static void agentmain(String agentArgs, Instrumentation inst)  {
        // 注册一个 ClassFileTransformer
        System.out.println("agentmain 执行了");
        inst.addTransformer(new MyClassFileTransformer(), true);
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class clazz : allLoadedClasses) {
            if (clazz.getName().equals("org.example.DemoController")) {
                try {
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }
}
