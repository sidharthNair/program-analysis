/* Generated By:JJTree: Do not edit this line. ASTMulop.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTMulop extends SimpleNode {

  String operator;

  public ASTMulop(int id) {
    super(id);
  }

  public ASTMulop(Lexer p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9d0dbe37de134b41e4f1d51ff41776e5 (do not edit this line) */
