import collection.JavaConversions._

class InterpreterVisitor(valueTable:ValueTable) extends SweetBaseVisitor[SweetObject] {

  override def visitAssign(ctx:SweetParser.AssignContext):SweetObject = {
    val obj = visitChildren(ctx)
    valueTable.set(new Value(ctx.ID.getText, obj))
    obj
  }

  override def visitFunctionDefinition(ctx:SweetParser.FunctionDefinitionContext):SweetObject = {
     new Function(this, ctx)
  }

  override def visitString(ctx:SweetParser.StringContext):SweetObject = {
    new StringObject(removeQuotes(ctx.STRING.getText))
  }

  override def visitId(ctx:SweetParser.IdContext):SweetObject = {
    valueTable.get(ctx.ID.getText)
  }

  override def visitFunctionCall(ctx:SweetParser.FunctionCallContext):SweetObject = {
    val args = ctx.formula.map(f => f.accept(this))
    valueTable.get(ctx.ID.getText).asInstanceOf[Function].call(args:_*)
  }
  
  def removeQuotes(str:String):String = str.substring(1, str.length() - 1)
}
