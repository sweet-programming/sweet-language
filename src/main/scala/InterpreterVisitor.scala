import collection.JavaConversions._

class InterpreterVisitor(functionTable:FunctionTable) extends SweetBaseVisitor[InterpreterContext] {
    private val context = new InterpreterContext

	override def visitAssign(ctx:SweetParser.AssignContext):InterpreterContext = {
        val functionDefinition = ctx.functionDefinition
        if (functionDefinition != null) {
            functionTable.set(new Function(ctx.ID().toString, this, functionDefinition))
        }
        visitChildren(ctx)
    }

	override def visitStatement(ctx:SweetParser.StatementContext):InterpreterContext = visitChildren(ctx)

    override def  visitFunctionCall(ctx:SweetParser.FunctionCallContext):InterpreterContext = {
        val values = ctx.formula.map(f => new StringValue(removeQuotes(f.STRING.getText)))
        functionTable.get(ctx.ID().toString()).call(values:_*)
        context;
    }
    
    def removeQuotes(str:String):String = str.substring(1, str.length() - 1)
}
