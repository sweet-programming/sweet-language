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

  override def visitStringValue(ctx:SweetParser.StringValueContext):SweetObject = {
    new StringObject(replaceEscapeSequence(removeQuotes(ctx.STRING.getText)))
  }

  override def visitIntValue(ctx:SweetParser.IntValueContext):SweetObject = {
    new IntObject(ctx.INT.getText.toInt)
  }

  override def visitValueRef(ctx:SweetParser.ValueRefContext):SweetObject = {
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

  override def visitDivMulOperation(ctx:SweetParser.DivMulOperationContext):SweetObject = {
    val left = visit(ctx.formula(0))
    val right = visit(ctx.formula(1))
    ctx.op.getText match {
      case "*" => left.asInstanceOf[IntObject].mul(right.asInstanceOf[IntObject])
      case "/" => left.asInstanceOf[IntObject].div(right.asInstanceOf[IntObject])
    }
  }

  override def visitAddSubOperation(ctx:SweetParser.AddSubOperationContext):SweetObject = {
    val left = visit(ctx.formula(0))
    val right = visit(ctx.formula(1))
    ctx.op.getText match {
      case "+" => left.add(right)
      case "-" => left.asInstanceOf[IntObject].sub(right.asInstanceOf[IntObject])
    }
  }

  override def visitEqualsOperation(ctx: SweetParser.EqualsOperationContext): SweetObject = {
    val left = visit(ctx.formula(0))
    val right = visit(ctx.formula(1))
    new BoolObject(left == right)
  }

  override def visitReturnStatement(ctx:SweetParser.ReturnStatementContext):SweetObject = {
    visit(ctx.formula)
  }
  
  def removeQuotes(str:String):String = str.substring(1, str.length() - 1)
  def replaceEscapeSequence(str:String):String = {
    "\\\\([n|r|t|b|f|\\\\])".r.replaceAllIn(str, m => {
      m.group(1) match {
        case "n" => "\n"
        case "r" => "\r"
        case "t" => "\t"
        case "b" => "\b"
      }
    })
  }
}
