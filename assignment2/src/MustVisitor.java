public class MustVisitor implements LexerVisitor {

    boolean found = false;

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStart node, Object data) {
        boolean foundMethodNameAndType = false;
        node.childrenAccept(this, foundMethodNameAndType);
        if (found) {
            System.out.println("SUCCESS: FOUND METHOD \"must\" WITH RETURN TYPE INT AND AT LEAST ONE ARGUMENT");
        }
        else {
            System.out.println("Did not find method \"must\" with return type int and at least one argument");
            System.exit(3);
        }
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
        boolean foundMethodNameAndType = (node.name.equals("must") && node.returnType.equals("int"));
        node.childrenAccept(this, foundMethodNameAndType);
        return null;
    }

    public Object visit(ASTFormPars node, Object data) {
        boolean foundMethodNameAndType = (boolean) data;
        if (foundMethodNameAndType) {
            if (node.jjtGetNumChildren() > 0) {
                found = true;
            }
        }
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
