public class TestingVisitor implements LexerVisitor {
    public TestingVisitor() {

    }

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStart node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTProgram node, Object data) {
        System.out.println("Program Name: " + node.name);
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTConstDecl node, Object data) {
        System.out.print("Const Type: " + node.type + " -- ");
        node.childrenAccept(this, data);
        System.out.println();
        return null;
    }

    public Object visit(ASTConst node, Object data) {
        System.out.print("(" + node.name + "," + node.value + ")");
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTEnumDecl node, Object data) {
        System.out.print("Enum Name: " + node.name + " -- ");
        node.childrenAccept(this, data);
        System.out.println();
        return null;
    }

    public Object visit(ASTEnum node, Object data) {
        System.out.print("(" + node.name + "," + node.value + ")");
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTVarDecl node, Object data) {
        System.out.print("Var Type: " + node.type + " -- ");
        node.childrenAccept(this, data);
        System.out.println();
        return null;
    }

    public Object visit(ASTVar node, Object data) {
        System.out.print("(" + node.name + "," + node.isArray + ")");
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStructDecl node, Object data) {
        System.out.println("Struct: " + node.name + ", parent: " + node.parent + ", implements: " + node.interfaces.toString());
        node.childrenAccept(this, data);
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
        System.out.print("Method: " + node.name + ", parameters: ");
        node.childrenAccept(this, data);
        System.out.println();
        return null;
    }

    public Object visit(ASTFormPars node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTParameter node, Object data) {
        System.out.print("(" + node.type + "," + node.name +  "," + node.isArray +")");
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
