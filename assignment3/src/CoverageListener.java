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

    private TreeMap<String, TreeSet<String>> coverageMap = new TreeMap<>();

    @Override
    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
        String path = instructionToExecute.getFileLocation();
        String pos = instructionToExecute.getFilePos();
        String[] split = pos.split(":");
        if (path != null && !path.startsWith("java/") && !path.startsWith("sun/") && !path.startsWith("gov/")) {
            if (!coverageMap.containsKey(split[0])) {
                coverageMap.put(split[0], new TreeSet<String>());
            }
            coverageMap.get(split[0]).add(split[1]);
        }
    }

    @Override
    public void searchFinished(Search search) {
        try {
            FileWriter report = new FileWriter("report.txt");
            for (String file : coverageMap.keySet()) {
                for (String lineNum : coverageMap.get(file)) {
                    report.write(file + ":" + lineNum + "\n");
                }
            }
            report.close();
        }
        catch (IOException e) {
            System.out.println("File IO Error occurred.");
            e.printStackTrace();
        }
    }
}