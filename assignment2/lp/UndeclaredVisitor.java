import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;

enum IdentifierType {
    SINGLE,
    DOT,
    ARRAY_INDEX,
    EXTENDS,
    IMPLEMENTS,
    RETURN_TYPE,
    DECL_TYPE,
}

public class UndeclaredVisitor implements LexerVisitor {

    int id;
    ScopeNode root;

    public UndeclaredVisitor() {
        id = 0;
    }

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStart node, Object data) {
        node.childrenAccept(this, data);
        root.printScopeTree("");
        System.out.println("SUCCESS: ALL NAMES DECLARED BEFORE USE");
        return null;
    }

    public Object visit(ASTProgram node, Object data) {
        root = new ScopeNode(id++, node.name);
        node.childrenAccept(this, root);
        return null;
    }

    public Object visit(ASTConstDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTConst node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        String type = ((ASTConstDecl) node.jjtGetParent()).type;
        if (curr.checkDeclared(type, IdentifierType.DECL_TYPE) == null) {
            halt(type);
        }
        curr.addSymbol(node.name, ((ASTConstDecl) node.jjtGetParent()).type);
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTEnumDecl node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        curr.addSymbol(node.name, "_enum");
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTEnum node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        curr.addSymbol(node.name, "_enum_val");
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTVarDecl node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTVar node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        String type = ((ASTVarDecl) node.jjtGetParent()).type;
        if (curr.checkDeclared(type, IdentifierType.DECL_TYPE) == null) {
            halt(type);
        }
        curr.addSymbol(node.name, type + (node.isArray ? "[]" : ""));
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTStructDecl node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        if (node.parent != null) {
            if (curr.checkDeclared(node.parent, IdentifierType.EXTENDS) == null) {
                halt(node.parent);
            }
        }
        for (String i : node.interfaces) {
            if (curr.checkDeclared(i, IdentifierType.IMPLEMENTS) == null) {
                halt(i);
            }
        }
        curr.addSymbol(node.name, "_struct");
        ScopeNode structScope = curr.newChildScope(id++, node.name, node.parent);
        node.childrenAccept(this, structScope);
        return null;
    }

    public Object visit(ASTInterfaceDecl node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        curr.addSymbol(node.name, "_interface");
        node.childrenAccept(this, curr.newChildScope(id++, node.name));
        return null;
    }

    public Object visit(ASTInterfaceMethodDecl node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        String returnType = node.returnType;
        if (curr.checkDeclared(returnType, IdentifierType.RETURN_TYPE) == null) {
            halt(returnType);
        }
        curr.addSymbol(node.name, "_interface_method");
        node.childrenAccept(this, curr.newChildScope(id++, node.name));
        return null;
    }

    public Object visit(ASTMethodDecl node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        String returnType = node.returnType;
        if (curr.checkDeclared(returnType, IdentifierType.RETURN_TYPE) == null) {
            halt(returnType);
        }
        curr.addSymbol(node.name, "_method");
        node.childrenAccept(this, curr.newChildScope(id++, node.name));
        return null;
    }

    public Object visit(ASTFormPars node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTParameter node, Object data) {
        ScopeNode curr = (ScopeNode) data;
        String type = node.type;
        if (curr.checkDeclared(type, IdentifierType.DECL_TYPE) == null) {
            halt(type);
        }
        curr.addSymbol(node.name, node.type + (node.isArray ? "[]" : ""));
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
        ScopeNode curr = (ScopeNode) data;
        String word = node.name;
        String next;
        if (node.modifiers.isEmpty()) {
            ScopeNode found = curr.checkDeclared(node.name, IdentifierType.SINGLE);
            // System.out.println(node.name + ": " + (found != null));
            if (found == null) {
                halt(node.name);
            }

        } else {
            String prefix = "";
            /*
            if (node.modifiers.size() > 1) {
                String command = node.name;
                prefix = "\t";
                for (int i = 0; i < node.modifiers.size(); i++) {
                    try {
                        Integer.parseInt(node.modifiers.get(i));
                        command += "[EXPR]";
                    } catch (Exception e) {
                        command += "." + node.modifiers.get(i);
                    }
                }
                System.out.println(command);
            }
            */
            for (int i = 0; i < node.modifiers.size(); i++) {
                try {
                    Integer.parseInt(node.modifiers.get(i));
                    curr = curr.checkDeclared(word, IdentifierType.ARRAY_INDEX);
                    // System.out.println(prefix + word + "[]: " + (curr != null));
                    if (curr == null) {
                        halt(word + "[EXPR]");
                    }
                } catch (Exception e) {
                    next = node.modifiers.get(i);
                    curr = curr.checkDeclared(word + "." + next, IdentifierType.DOT);
                    // System.out.println(prefix + word + "." + next + ": " + (curr != null));
                    if (curr == null) {
                        halt(word + "." + next);
                    }
                    word = next;
                }
            }
        }
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

    public void halt(String var) {
        root.printScopeTree("");
        System.out.println("Found undeclared reference: " + var);
        System.exit(1);
    }
}

class ScopeNode extends SimpleNode {

    static HashSet<String> primitives = new HashSet<String>(Arrays.asList("int", "boolean", "char"));
    static HashSet<String> predefinedMethods = new HashSet<String>(Arrays.asList("ord", "chr", "length"));
    static HashMap<String, ScopeNode> structMap = new HashMap<String, ScopeNode>();

    String scopeName;
    String structParent;
    HashMap<String, String> symbolTable;

    public ScopeNode(int i, String n) {
        super(i);
        symbolTable = new HashMap<String, String>();
        scopeName = n;
    }

    // Non-structure scope
    public ScopeNode newChildScope(int id, String name) {
        ScopeNode child = new ScopeNode(id, name);
        child.jjtSetParent(this);
        this.jjtAddChild(child, this.jjtGetNumChildren());
        return child;
    }

    // If a structParent object is passed then we know this scope defines a
    // structure
    public ScopeNode newChildScope(int id, String name, String structParent) {
        ScopeNode child = new ScopeNode(id, name);
        child.structParent = structParent;
        child.addSymbol("this", child.scopeName);
        structMap.put(child.scopeName, child);
        child.jjtSetParent(this);
        this.jjtAddChild(child, this.jjtGetNumChildren());
        return child;
    }

    public void addSymbol(String name, String declaredType) {
        this.symbolTable.put(name, declaredType);
    }

    // Returns the scope where the variable / method is found
    public ScopeNode checkDeclared(String identifier, IdentifierType dt) {
        ScopeNode structSearch = null;
        switch (dt) {
            case SINGLE:
                if (predefinedMethods.contains(identifier)) {
                    // ord, chr, or length
                    return this;
                }
                if (symbolTable.containsKey(identifier) &&
                        (primitives.contains(symbolTable.get(identifier))
                                || structMap.containsKey(symbolTable.get(identifier))
                                || symbolTable.get(identifier).equals("_method"))) {
                    // in symbol table; primitive, struct instance, or method
                    return this;
                }
                if (structMap.containsKey(this.scopeName)) {
                    structSearch = searchStructHierarchy(this.scopeName, identifier);
                    if (structSearch != null) {
                        return structSearch;
                    }
                }
                break;
            case DOT:
                String[] words = identifier.split("[.]");
                String first = words[0];
                String second = words[1];
                if (symbolTable.containsKey(first) &&
                        symbolTable.containsKey(second) &&
                        symbolTable.get(first).equals("_enum") &&
                        symbolTable.get(second).equals("_enum_val")) {
                    // ENUM.ENUM_VAL
                    return this;
                }
                if (first.equals("this") && structMap.containsKey(this.scopeName)) {
                    structSearch = searchStructHierarchy(this.scopeName, second);
                    if (structSearch != null) {
                        return structSearch;
                    }
                }
                if (symbolTable.containsKey(first)) {
                    String firstType = symbolTable.get(first);
                    if (firstType.contains("[]")) {
                        firstType = firstType.substring(0, firstType.indexOf("[]"));
                    }
                    if (structMap.containsKey(firstType)) {
                        // STRUCT_VAR.{METHOD | VAR}
                        return searchStructHierarchy(firstType, second);
                    }
                }
                break;
            case ARRAY_INDEX:
                if (symbolTable.containsKey(identifier)
                        && symbolTable.get(identifier).contains("[]")) {
                    return this;
                }
                break;
            case EXTENDS:
                if (symbolTable.containsKey(identifier)
                        && symbolTable.get(identifier).equals("_struct")) {
                    return this;
                }
                break;
            case IMPLEMENTS:
                if (symbolTable.containsKey(identifier)
                        && symbolTable.get(identifier).equals("_interface")) {
                    return this;
                }
                break;
            case RETURN_TYPE:
                if (identifier.equals("void")) {
                    return this;
                }
                // fall through
            case DECL_TYPE:
                if (primitives.contains(identifier)) {
                    return this;
                }
                if (symbolTable.containsKey(identifier)
                        && (symbolTable.get(identifier).equals("_struct")
                            || symbolTable.get(identifier).equals("_enum"))) {
                    return this;
                }
                break;
            default:
                break;
        }
        if (this.id == 0) {
            // Reached top of tree and haven't found variable declared
            return null;
        } else {
            return ((ScopeNode) this.jjtGetParent()).checkDeclared(identifier, dt);
        }
    }

    // Recursively searches struct hierarchy for word starting at struct structName
    public static ScopeNode searchStructHierarchy(String structName, String word) {
        ScopeNode structScope = structMap.get(structName);
        do {
            if (structScope.symbolTable.containsKey(word)) {
                return structScope;
            }
            structScope = structMap.get(structScope.structParent);
        } while (structScope != null);
        return null;
    }

    // Prints scope tree rooted at this node
    public void printScopeTree(String prefix) {
        System.out.print(prefix + scopeName + ": { ");
        for (String k : symbolTable.keySet()) {
            System.out.print(symbolTable.get(k) + ":" + k + " ");
        }
        System.out.println("}");
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            ((ScopeNode) this.jjtGetChild(i)).printScopeTree(prefix + "  ");
        }
    }
}
