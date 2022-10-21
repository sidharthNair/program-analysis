import gov.nasa.jpf.jvm.ClassFile;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.SearchListener;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.DynamicElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.VMListener;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.util.ObjectList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MemoizationListener extends ListenerAdapter {

    // Outer hashmap maps method to arguments map, which maps arguments to output
    HashMap<MethodInfo, HashMap<String, Object>> memoizeMap = new HashMap<>();
    // Maps stack depth to arguments for a certain method
    HashMap<ThreadInfo, Stack<Object[]>> argumentMap = new HashMap<>();

    File file = new File("results/report2.txt");
    FileWriter report;

    @Override
    public void searchStarted(Search search) {
        file.getParentFile().mkdirs();
        try {
            report = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchFinished(Search search) {
        try {
            report.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
        if (instructionToExecute instanceof JVMInvokeInstruction) {
            JVMInvokeInstruction inst = (JVMInvokeInstruction) instructionToExecute;
            MethodInfo mi = inst.getInvokedMethod(currentThread);
            if (mi.isStatic() && isPrimitive(mi.getReturnTypeName())) {
                Object[] args = inst.getArgumentValues(currentThread);
                String checkSum = checkSum(args);
                if (memoizeMap.containsKey(mi) && memoizeMap.get(mi).containsKey(checkSum)) {
                    StackFrame frame = currentThread.getModifiableTopFrame();
                    frame.removeArguments(mi);
                    Object result = memoizeMap.get(mi).get(checkSum);
                    try {
                        report.write("Returning memoized return value for " + methodToString(mi, args, result) + ".\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (result instanceof Long) {
                        frame.pushLong((Long) result);
                    } else if (result instanceof Double) {
                        frame.pushDouble((Double) result);
                    } else if (result instanceof Float) {
                        frame.pushFloat((Float) result);
                    } else {
                        frame.push((Integer) result);
                    }
                    currentThread.skipInstruction(currentThread.getPC().getNext());
                }
            }
        }
    }

    @Override
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction,
            Instruction executedInstruction) {
        if (!currentThread.isInstructionSkipped() && executedInstruction instanceof JVMInvokeInstruction) {
            JVMInvokeInstruction inst = (JVMInvokeInstruction) executedInstruction;
            Object[] args = inst.getArgumentValues(currentThread);
            if (!argumentMap.containsKey(currentThread)) {
                argumentMap.put(currentThread, new Stack<Object[]>());
            }
            argumentMap.get(currentThread).push(args);
        }
        if (executedInstruction instanceof JVMReturnInstruction) {
            MethodInfo mi = executedInstruction.getMethodInfo();
            Object[] args = argumentMap.get(currentThread).pop();
            Object returnValue;
            String type = mi.getReturnTypeName();
            if (type.equals("void")) {
                returnValue = null;
            } else if (type.equals("long")) {
                returnValue = currentThread.getTopFrame().peekLong();
            } else if (type.equals("double")) {
                returnValue = currentThread.getTopFrame().peekDouble();
            } else if (type.equals("float")) {
                returnValue = currentThread.getTopFrame().peekFloat();
            } else {
                returnValue = currentThread.getTopFrame().peek();
            }
            if (mi.isStatic() && isPrimitive(mi.getReturnTypeName())) {
                String checkSum = checkSum(args);
                if (!memoizeMap.containsKey(mi)) {
                    memoizeMap.put(mi, new HashMap<String, Object>());
                }
                try {
                    report.write("Memoizing " + methodToString(mi, args, returnValue) + ".\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                memoizeMap.get(mi).put(checkSum, returnValue);
            } else {
                try {
                    report.write(methodToString(mi, args, returnValue) + " is not memoizable" + ".\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String methodToString(MethodInfo mi, Object[] args, Object returnValue) {
        String methodName = mi.getName();
        String params = "";
        if (args != null) {
            for (Object arg : args) {
                if (arg != null) {
                    if (isPrimitiveClass(arg.getClass())) {
                        params += arg;
                    } else {
                        params += "object:" + ((DynamicElementInfo) arg).getClassInfo().getName();
                    }
                    params += ", ";
                }
            }
            if (params.length() > 0) {
                params = params.substring(0, params.length() - 2);
            }
        }
        String returnString = "";
        if (returnValue != null && !isPrimitiveClass(returnValue.getClass())) {
            returnString += "object:" + returnValue.getClass().getName();
        } else if (returnValue != null) {
            returnString += returnValue;
        } else {
            returnString += "void";
        }
        return methodName + "(" + params + "):" + returnString;
    }

    public static String checkSum(Object[] args) {
        String ret = "";
        if (args != null) {
            for (Object arg : args) {
                ret += checkSumHelper(arg, new HashSet<Object>()) + " ";
            }
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    public static String checkSumHelper(Object arg, HashSet<Object> visited) {
        if (arg == null) {
            return null;
        }
        if (visited.contains(arg)) {
            // recursive reference
            return "rref";
        }
        visited.add(arg);
        String ret = "";
        if (isPrimitiveClass(arg.getClass())) {
            ret += arg;
        } else {
            if (arg instanceof DynamicElementInfo) {
                DynamicElementInfo e = (DynamicElementInfo) arg;
                ret += "(";
                for (int i = 0; i < e.getNumberOfFields(); i++) {
                    FieldInfo f = e.getFieldInfo(i);
                    if (f.isReference()) {
                        ret += checkSumHelper(f.getValueObject(e.getFields()), visited) + ",";
                    } else {
                        ret += f.getValueObject(e.getFields()) + ",";
                    }
                }
                ret = ret.substring(0, ret.length() - 1) + ")";
            }
        }
        visited.remove(arg);
        return ret;
    }

    public static boolean isPrimitive(String type) {
        return type.equals("byte")
                || type.equals("short")
                || type.equals("int")
                || type.equals("long")
                || type.equals("float")
                || type.equals("double")
                || type.equals("char")
                || type.equals("boolean");
    }

    public static boolean isPrimitiveClass(Class<?> type) {
        return (type.isPrimitive()) ||
                type == Byte.class || type == Short.class || type == Integer.class ||
                type == Long.class || type == Float.class || type == Double.class ||
                type == Character.class || type == Boolean.class;
    }
}