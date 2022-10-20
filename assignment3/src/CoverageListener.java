import gov.nasa.jpf.jvm.ClassFile;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.SearchListener;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.VMListener;
import gov.nasa.jpf.ListenerAdapter;
import java.util.TreeSet;
import java.util.TreeMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CoverageListener extends ListenerAdapter {

    private TreeMap<String, TreeSet<Integer>> coverageMap = new TreeMap<>();

    @Override
    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
        String path = instructionToExecute.getFileLocation();
        int lineNum = instructionToExecute.getLineNumber();
        String className = instructionToExecute.getMethodInfo().getClassInfo().getName();
        if (path != null) {
            if (!coverageMap.containsKey(className)) {
                coverageMap.put(className, new TreeSet<Integer>());
            }
            coverageMap.get(className).add(lineNum);
        }
    }

    @Override
    public void searchFinished(Search search) {
        try {
            File file = new File("results/report.txt");
            file.getParentFile().mkdirs();
            FileWriter report = new FileWriter(file);
            for (String className : coverageMap.keySet()) {
                for (Integer lineNum : coverageMap.get(className)) {
                    report.write(className + ":" + lineNum + "\n");
                }
            }
            report.close();
        } catch (IOException e) {
            System.out.println("File IO Error occurred.");
            e.printStackTrace();
        }
    }
}