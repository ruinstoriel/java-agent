package org.example.javaagent;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

public class Attach {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("Application")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("D:\\kotlin\\java-agent\\target\\java-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
                //.....
                // virtualMachine.detach();
            }
        }

    }
}
