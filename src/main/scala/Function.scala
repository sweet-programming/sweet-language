class Function(visitor:InterpreterVisitor, definition:SweetParser.FunctionDefinitionContext) extends SweetObject {
  val objectType = "function"
  
  def call(args:SweetObject*):SweetObject = {
      visitor.visitChildren(definition)
      null
  }
}
