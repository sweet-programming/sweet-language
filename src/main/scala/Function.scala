import collection.JavaConversions._

class Function(p: Scope, d:SweetParser.FunctionDefinitionContext) extends SweetObject {
  val parentScope = p
  val definition = d
  val objectType = "function"
  
  def call(args:SweetObject*):SweetObject = {
    val scope = new Scope(parentScope)
    declareValues(scope)
    val visitor = new ExecutionVisitor(scope)

    if (definition.argList != null) {
      for (i <- 0 until definition.argList.ID.size) {
        scope.define(new Value(definition.argList.ID(i).getText, args(i)))
      }
    }

    var ctx: InterpreterContext = null
    for (s <- definition.statement) {
      ctx = s.accept(visitor)
      if (ctx.returnStatement) return ctx.value
    }
    ctx.value
  }

  def declareValues(scope: Scope): Unit = {
    val visitor = new DeclareVisitor(scope)
    for (s <- definition.statement) {
      visitor.visit(s)
    }
  }

  override def toString():String = "@()"
  override def add(o:SweetObject):SweetObject = sys.error("function can't add")
}

class DeclareVisitor(s:Scope) extends SweetBaseVisitor[Unit] {
  val scope = s

  override def visitAssignValue(ctx:SweetParser.AssignValueContext):Unit = {
    scope.declare(ctx.ID.getText)
  }
}

class ExecutionVisitor(s: Scope) extends SweetBaseVisitor[InterpreterContext] {
  val scope = s
  val interpreter = new InterpreterVisitor(scope)

  override def visitReturnExpression(ctx: SweetParser.ReturnExpressionContext): InterpreterContext = {
    val value = ctx.formula.accept(interpreter)
    new InterpreterContext(value, true)
  }

  override def visitFormulaExpression(ctx: SweetParser.FormulaExpressionContext): InterpreterContext = {
    val value = ctx.formula.accept(interpreter)
    new InterpreterContext(value)
  }

  override def visitIfStatement(ctx: SweetParser.IfStatementContext): InterpreterContext = {
    val condition = ctx.formula.accept(interpreter).asInstanceOf[BoolObject]
    if (condition.value) {
      visit(ctx.expression)
    } else {
      new InterpreterContext(NullObject)
    }
  }

  override def visitPostIfStatement(ctx: SweetParser.PostIfStatementContext): InterpreterContext = {
    val condition = ctx.formula.accept(interpreter).asInstanceOf[BoolObject]
    if (condition.value) {
      visit(ctx.expression)
    } else {
      new InterpreterContext(NullObject)
    }
  }

  override def visitUnlessStatement(ctx: SweetParser.UnlessStatementContext): InterpreterContext = {
    val condition = ctx.formula.accept(interpreter).asInstanceOf[BoolObject]
    if (!condition.value) {
      visit(ctx.expression)
    } else {
      new InterpreterContext(NullObject)
    }
  }

  override def visitPostUnlessStatement(ctx: SweetParser.PostUnlessStatementContext): InterpreterContext = {
    val condition = ctx.formula.accept(interpreter).asInstanceOf[BoolObject]
    if (!condition.value) {
      visit(ctx.expression)
    } else {
      new InterpreterContext(NullObject)
    }
  }
}
