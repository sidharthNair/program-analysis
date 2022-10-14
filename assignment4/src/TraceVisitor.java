import java.nio.file.Files;
import java.io.File;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MVisitor extends MethodVisitor {

    private String className;
    private String methodName;
    private String desc;

    public MVisitor(MethodVisitor mv, String className, String methodName, String desc) {
        super(Opcodes.ASM5, mv);
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
    }

    @Override
    public void visitCode() {
        mv.visitTypeInsn(Opcodes.NEW, "java/io/FileWriter");
        mv.visitInsn(Opcodes.DUP);
        mv.visitTypeInsn(Opcodes.NEW, "java/io/File");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("raw_output");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/io/FileWriter", "<init>", "(Ljava/io/File;Z)V", false);
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(className + "." + methodName + ":" + desc + "\n");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/FileWriter", "write", "(Ljava/lang/String;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/FileWriter", "close", "()V", false);

        super.visitCode();
    }
}

public class TraceVisitor extends ClassVisitor {

    private String className;

    public TraceVisitor(ClassWriter cw, String className) {
        super(Opcodes.ASM5, cw);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (!name.equals("<init>") && !name.equals("<clinit>")) {
            mv = new MVisitor(mv, className, name, desc);
        }
        return mv;
    }
}
