import java.util.HashMap;
import java.util.HashSet;

public class DuplicateStructMethodVisitor implements LexerVisitor {

    HashMap<String, HashSet<String>> structMethods = new HashMap<String, HashSet<String>>();

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStart node, Object data) {
        String structName = null;
        node.childrenAccept(this, structName);
        System.out.println("\nSUCCESS: NO DUPLICATE METHOD NAMES USED IN A SINGLE STRUCT\n");
        return null;
    }

    public Object visit(ASTProgram node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTConstDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTConst node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTEnumDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTEnum node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTVarDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTVar node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStructDecl node, Object data) {
        structMethods.put(node.name, new HashSet<String>());
        node.childrenAccept(this, node.name);
        return null;
    }

    public Object visit(ASTInterfaceDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTInterfaceMethodDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTMethodDecl node, Object data) {
        String structName = (String) data;
        if (structName != null) {
            if (!structMethods.get(structName).add(node.name)) {
                System.out.println("Found duplicate method name in struct " + structName + ": " + node.name);
                System.exit(2);
            }
        }
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTFormPars node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTParameter node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStmt node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTDesignatorStmt node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTActPars node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTCondition node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTCondTerm node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTCondFact node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTExpression node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTTerm node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTFactor node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTDesignator node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTAssignop node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTRelop node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTAddop node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTMulop node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }
}
