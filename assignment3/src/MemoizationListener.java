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

import java.lang.reflect.Field;

public class MemoizationListener extends ListenerAdapter {

    // Outer hashmap maps method to arguments map, which maps arguments to output
    HashMap<MethodInfo, HashMap<String, Object>> memoizeMap = new HashMap<>();
    // Maps stack depth to arguments for a certain method
    HashMap<MethodInfo, HashMap<Integer, String>> argumentMap = new HashMap<>();

    @Override
    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
        if (instructionToExecute instanceof JVMInvokeInstruction) {
            JVMInvokeInstruction inst = (JVMInvokeInstruction) instructionToExecute;
            MethodInfo mi = inst.getInvokedMethod(currentThread);
            if (mi.isStatic() && isPrimitive(mi.getReturnTypeName())) {
                String checkSum = "";
                Object[] args = inst.getArgumentValues(currentThread);
                if (args != null) {
                    for (Object arg : args) {
                        checkSum += checkSum(arg, new HashSet<Object>()) + " ";
                    }
                }
                checkSum = checkSum.substring(0, checkSum.length() - 1);
                if (memoizeMap.containsKey(mi) && memoizeMap.get(mi).containsKey(checkSum)) {
                    StackFrame frame = currentThread.getModifiableTopFrame();
                    if (args != null) {
                        for (Object arg : args) {
                            frame.pop();
                        }
                    }
                    Object result = memoizeMap.get(mi).get(checkSum);
                    if (result instanceof Long) {
                        frame.pushLong((Long) result);
                    }
                    else if (result instanceof Double) {
                        frame.pushDouble((Double) result);
                    }
                    else if (result instanceof Float) {
                        frame.pushFloat((Float) result);
                    }
                    else {
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
        if (executedInstruction instanceof JVMInvokeInstruction) {
            JVMInvokeInstruction inst = (JVMInvokeInstruction) executedInstruction;
            MethodInfo mi = inst.getInvokedMethod(currentThread);
            if (mi.isStatic() && isPrimitive(mi.getReturnTypeName())) {
                String checkSum = "";
                Object[] args = inst.getArgumentValues(currentThread);
                if (args != null) {
                    for (Object arg : args) {
                        checkSum += checkSum(arg, new HashSet<Object>()) + " ";
                    }
                }
                checkSum = checkSum.substring(0, checkSum.length() - 1);
                if (!argumentMap.containsKey(mi)) {
                    argumentMap.put(mi, new HashMap<Integer, String>());
                }
                argumentMap.get(mi).put(currentThread.getStackDepth() - 1, checkSum);
            }
        }
        if (executedInstruction instanceof JVMReturnInstruction) {
            MethodInfo mi = executedInstruction.getMethodInfo();
            if (argumentMap.containsKey(mi)) {
                String checkSum = argumentMap.get(mi).remove(currentThread.getStackDepth());
                if (checkSum != null) {
                    if (!memoizeMap.containsKey(mi)) {
                        memoizeMap.put(mi, new HashMap<String, Object>());
                    }
                    memoizeMap.get(mi).put(checkSum, currentThread.getTopFrame().peek());
                }
            }
        }
    }

    @Override
    public void searchFinished(Search search) {

    }

    public static String checkSum(Object arg, HashSet<Object> visited) {
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
                        ret += checkSum(f.getValueObject(e.getFields()), visited) + ",";
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
                type == Character.class || type == Boolean.class || type == String.class;
    }
}