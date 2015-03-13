import collection.JavaConversions._

class Function(p: Scope, d:SweetParser.FunctionDefinitionContext) extends SweetObject {
  val parentScope = p
  val definition = d
  val objectType = "function"
  
  def call(args:SweetObject*):SweetObject = {
    val scope = new Scope(parentScope)
    declareValues(scope)
    val visitor = new InterpreterVisitor(scope)

    if (definition.argList != null) {
      for (i <- 0 until definition.argList.ID.size) {
        scope.define(new Value(definition.argList.ID(i).getText, args(i)))
      }
    }
    var result:SweetObject = null
    for (s <- definition.statement) {
      val (res, statement) = executeStatement(s, visitor)
      if (res != null) result = res
      if (statement.isInstanceOf[SweetParser.ReturnStatementContext]) return result
    }
    result
  }

  def declareValues(scope: Scope): Unit = {
    val visitor = new DeclareVisitor(scope)
    for (s <- definition.statement) {
      visitor.visit(s)
    }
  }

  def executeStatement(s: SweetParser.StatementContext, visitor: InterpreterVisitor): (SweetObject, SweetParser.StatementContext) = {
    var result: SweetObject = null
    var statement = s
    if (s.isInstanceOf[SweetParser.IfStatementContext]) {
      val ifStatement = s.asInstanceOf[SweetParser.IfStatementContext]
      val condition = ifStatement.formula.accept(visitor)
      if (condition.asInstanceOf[BoolObject].value) {
        statement = ifStatement.statement
        result = statement.accept(visitor)
      }
    } else if (s.isInstanceOf[SweetParser.UnlessStatementContext]) {
      val unlessStatement = s.asInstanceOf[SweetParser.UnlessStatementContext]
      val condition = unlessStatement.formula.accept(visitor)
      if (!condition.asInstanceOf[BoolObject].value) {
        statement = unlessStatement.statement
        result = statement.accept(visitor)
      }
    } else {
       result = statement.accept(visitor)
    }
    (result, statement)
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
