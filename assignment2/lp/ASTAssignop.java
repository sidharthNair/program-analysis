/* Generated By:JJTree: Do not edit this line. ASTAssignop.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTAssignop extends SimpleNode {
  public ASTAssignop(int id) {
    super(id);
  }

  public ASTAssignop(Lexer p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f286f5ba89e28d1f1b41550443604fb4 (do not edit this line) */
