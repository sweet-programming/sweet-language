import collection.JavaConversions._

class Function(parentScope: Scope, d:SweetParser.FunctionDefinitionContext) extends SweetObject {
  val definition = d
  val objectType = "function"
  val scope = new Scope(parentScope)
  
  def call(args:SweetObject*):SweetObject = {
    if (definition.argList != null) {
      for (i <- 0 until definition.argList.ID.size) {
        scope.define(new Value(definition.argList.ID(i).getText, args(i)))
      }
    }
    val visitor = new InterpreterVisitor(scope)
    var result:SweetObject = null
    for (s <- definition.statement) {
      result = s.accept(visitor)
    }
    result
  }

  override def toString():String = "@()"
  override def add(o:SweetObject):SweetObject = sys.error("function can't add")
}
