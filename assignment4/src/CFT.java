
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CFT implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className != null && !className.startsWith ("jdk/") && !className.startsWith ("java/") && !className.startsWith("sun/") && !className.startsWith("gov/")) {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

            // 2. modify the IR

            TraceVisitor tv = new TraceVisitor(cw, className);
            cr.accept(tv, 0);

            // 3. return array of bytes
            return cw.toByteArray();
        } else {
            return null;
        }
    }
}
