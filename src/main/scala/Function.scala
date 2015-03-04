class Function(n:String, visitor:InterpreterVisitor, definition:SweetParser.FunctionDefinitionContext) {
    val name = n
    def call(args:Value*):Value = {
        visitor.visitChildren(definition)
        null
    }
}
