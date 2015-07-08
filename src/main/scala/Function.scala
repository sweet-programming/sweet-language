import collection.JavaConversions._

class Function(p: Scope, d: SweetParser.FunctionDefinitionContext) extends SweetObject {
  val parentScope = p
  val definition = d
  val objectType = "function"

  val memo = createMemo(d)

  def bindArgs(scope: Scope, args: List[SweetObject], parametricArgs: List[Value]) {
    if (definition.argList != null) {
      var varArgs = args
      for (i <- 0 until definition.argList.ID.size) {
        val name = definition.argList.ID(i).getText
        val value = parametricArgs.find(arg => arg.name == name).getOrElse(
           {
              val new_value = new Value(name, varArgs.head)
              varArgs = varArgs.drop(1)
              new_value
           })
        scope.define(value)
      }
    }
  }

  def call(args: List[SweetObject], parametricArgs: List[Value]): SweetObject = {
    memo.getResultValue(args) match {
      case null => {
        val result = execute(args, parametricArgs)
        memo.set(args, result)
        result
      }
      case x => x
    }
  }

  def execute(args: List[SweetObject], parametricArgs: List[Value]): SweetObject = {
    val scope = new Scope(parentScope)
    declareValues(scope)
    val visitor = new ExecutionVisitor(scope)

    bindArgs(scope, args, parametricArgs)

    var ctx: InterpreterContext = null
    definition.statement foreach { s =>
      ctx = s.accept(visitor)
      if (ctx.returnStatement) return ctx.value
    }
    ctx.value
  }

  def declareValues(scope: Scope): Unit = {
    val visitor = new DeclareVisitor(scope)
    definition.statement foreach { s =>
      visitor.visit(s)
    }
  }

  def createMemo(d: SweetParser.FunctionDefinitionContext): Memo = {
    if (d == null || d.argList == null) return new Memo(List())
    val suffixes = d.argList.argTypeSuffix
    val visitor = new InterpreterVisitor(new Scope(parentScope))
    val size = suffixes.map { suffix =>
      // todo: error handling is nessesary
      if (suffix.formula(0) == null) return new Memo(List())
      val start = suffix.formula(0).accept(visitor).asInstanceOf[IntObject].value
      val end = suffix.formula(1).accept(visitor).asInstanceOf[IntObject].value
      end - start + 1
    }.toList

    new Memo(size)
  }

  override def toString(): String = "@()"
  override def add(o:SweetObject): SweetObject = sys.error("function can't add")
}

class Memo(s: List[Int]) {
  val size = s
  val array = new Array[SweetObject](size.foldLeft(1) { (s, u) => u * s })

  def getResultValue(args: List[SweetObject]): SweetObject = {
    if (size.length == 0) return null
    array(index(args))
  }

  def set(args: List[SweetObject], result: SweetObject): Unit = {
    if (size.length == 0) return
    array(index(args)) = result
  }

  def index(args: List[SweetObject]): Int = {
    args.foldLeft((0, 1, 1)) { (sum, arg) =>
      val v = arg.asInstanceOf[IntObject].value
      val i = sum._1 + sum._2 * v
      val p = if (sum._3 < size.length) sum._2 * size(sum._3) else 0
      (i, p, sum._3 + 1)
    }._1
  }
}

class DeclareVisitor(s: Scope) extends SweetBaseVisitor[Unit] {
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

  override def visitExpressionStatement(ctx: SweetParser.ExpressionStatementContext): InterpreterContext = {
    visit(ctx.expression)
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

class BindFunction(f: Function, b: List[Value]) extends Function(f.parentScope, f.definition) {
  val parent = f
  val bindings = b

  override def call(args: List[SweetObject], parametricArgs: List[Value]): SweetObject = {
    super.call(args, parametricArgs:::bindings)
  }
}
