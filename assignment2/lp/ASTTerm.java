/* Generated By:JJTree: Do not edit this line. ASTTerm.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTTerm extends SimpleNode {
  public ASTTerm(int id) {
    super(id);
  }

  public ASTTerm(Lexer p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=91f471c55df9f5e5c4de7f7016192b21 (do not edit this line) */