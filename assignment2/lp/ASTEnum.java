/* Generated By:JJTree: Do not edit this line. ASTEnum.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTEnum extends SimpleNode {

  String name;
  Integer value;

  public ASTEnum(int id) {
    super(id);
  }

  public ASTEnum(Lexer p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(LexerVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=de4a047899515bd40639db0d3f089541 (do not edit this line) */
