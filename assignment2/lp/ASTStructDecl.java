/* Generated By:JJTree: Do not edit this line. ASTStructDecl.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
import java.util.ArrayList;

public class ASTStructDecl extends SimpleNode {

  String name;
  String parent;
  ArrayList<String> interfaces;

  public ASTStructDecl(int id) {
    super(id);
    interfaces = new ArrayList<String>();
  }

  public ASTStructDecl(Lexer p, int id) {
    super(p, id);
    interfaces = new ArrayList<String>();
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7c61eef9f1e909410198aacfdbb1aca3 (do not edit this line) */
