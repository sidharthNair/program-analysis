
import java.lang.instrument.Instrumentation;

public class DemoAgent {

    private static Instrumentation sInstrumentation;

    public static void premain(String options, Instrumentation instrumentation) {
        sInstrumentation = instrumentation;
        instrumentation.addTransformer(new CFT());
    }
}
