import collection.JavaConversions._

class InterpreterVisitor(s:Scope) extends SweetBaseVisitor[SweetObject] {
  val scope = s

  override def visitAssign(ctx:SweetParser.AssignContext):SweetObject = {
    val obj = visitChildren(ctx)
    scope.define(new Value(ctx.ID.getText, obj))
    obj
  }

  override def visitFunctionDefinition(ctx:SweetParser.FunctionDefinitionContext):SweetObject = {
     new Function(scope, ctx)
  }

  override def visitString(ctx:SweetParser.StringContext):SweetObject = {
    new StringObject(removeQuotes(ctx.STRING.getText))
  }

  override def visitVar(ctx:SweetParser.VarContext):SweetObject = {
    scope.resolve(ctx.ID.getText)
  }

  override def visitFunctionCall(ctx:SweetParser.FunctionCallContext):SweetObject = {
    val args = if (ctx.formList == null) {
      List.empty[SweetObject]
    } else {
      ctx.formList.formula.map(f => f.accept(this))
    }
    scope.resolve(ctx.ID.getText).asInstanceOf[Function].call(args:_*)
  }

  override def visitFunctionCall2(ctx:SweetParser.FunctionCall2Context):SweetObject = {
    val args = ctx.formList.formula.map(f => f.accept(this))
    scope.resolve(ctx.ID.getText).asInstanceOf[Function].call(args:_*)
  }
  
  def removeQuotes(str:String):String = str.substring(1, str.length() - 1)
}
