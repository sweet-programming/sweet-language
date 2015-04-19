import collection.JavaConversions._

class InterpreterVisitor(s:Scope) extends SweetBaseVisitor[SweetObject] {
  val scope = s

  override def visitAssignValue(ctx:SweetParser.AssignValueContext):SweetObject = {
    val obj = visit(ctx.formula)
    scope.define(new Value(ctx.ID.getText, obj))
    obj
  }

  override def visitAssignArray(ctx:SweetParser.AssignArrayContext):SweetObject = {
    val index = visit(ctx.arrayAccessor.formula).asInstanceOf[IntObject]
    val array = visit(ctx.formula(0)).asInstanceOf[ArrayObject]
    val obj = visit(ctx.formula(1))
    array.set(index.value, obj)
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

  override def visitArrayRef(ctx: SweetParser.ArrayRefContext):SweetObject = {
    val array = visit(ctx.formula).asInstanceOf[ArrayObject]
    val index = visit(ctx.arrayAccessor.formula).asInstanceOf[IntObject]
    array.get(index.value)
  }

  override def visitValueRef(ctx:SweetParser.ValueRefContext):SweetObject = {
    try {
      scope.resolve(ctx.ID.getText)
    } catch { case e: Exception =>
      sys.error(e.getMessage + "=> " + ctx.getStart.getLine + ":" + ctx.getStart.getCharPositionInLine)
    }
  }

  override def visitFunctionCall(ctx: SweetParser.FunctionCallContext): SweetObject = {
    val args = ctx.formList match {
      case null => List.empty[SweetObject]
      case x => x.formula.map(f => f.accept(this)).toList
    }
    val func = visit(ctx.formula).asInstanceOf[Function]
    func.call(args, List.empty[Value])
  }

  override def visitPartialApplication(ctx: SweetParser.PartialApplicationContext): SweetObject = {
    val bindings = ctx.bindingList.binding.map { b => new Value(b.ID.getText, visit(b.formula)) }.toList
    val func = visit(ctx.formula).asInstanceOf[Function]
    new BindFunction(func, bindings)
  }

  override def visitIsIdDefined(ctx: SweetParser.IsIdDefinedContext) = {
     new BoolObject(scope.isDefined(ctx.IIDD.getText.dropRight(1)))
  }

  override def visitIsArrayDefined(ctx: SweetParser.IsArrayDefinedContext) = {
     val array = visit(ctx.formula(0)).asInstanceOf[ArrayObject]
     val index = visit(ctx.formula(1)).asInstanceOf[IntObject]
     new BoolObject(array.isDefined(index.value))
  }

  override def visitDivMulOperation(ctx:SweetParser.DivMulOperationContext):SweetObject = {
    val left = visit(ctx.formula(0))
    val right = visit(ctx.formula(1))
    ctx.op.getText match {
      case "*" => left.asInstanceOf[IntObject].mul(right.asInstanceOf[IntObject])
      case "/" => left.asInstanceOf[IntObject].div(right.asInstanceOf[IntObject])
    }
  }

  override def visitAddSubOperation(ctx: SweetParser.AddSubOperationContext): SweetObject = {
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
    new BoolObject(left.equals(right))
  }

  override def visitParenthesis(ctx: SweetParser.ParenthesisContext): SweetObject = {
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
