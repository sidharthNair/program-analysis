/* Generated By:JJTree: Do not edit this line. ASTConst.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTConst extends SimpleNode {

  String name;
  String value;

  public ASTConst(int id) {
    super(id);
  }

  public ASTConst(Lexer p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c780870b1c0b84247c557c4edfe92a58 (do not edit this line) */
