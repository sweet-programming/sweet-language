import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._
import java.io.{ FileInputStream, InputStream, PrintStream }

object Sweet {
  private var output = System.out

  def main(args: Array[String]) {
    val is = if (args.length > 0) new FileInputStream(args(0)) else System.in
    run(is, System.out)
  }

  def run(in: InputStream, out: PrintStream) {
    this.output = out
    val input = new ANTLRInputStream(in)
    val lexer = new SweetLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new SweetParser(tokens)
    val tree = parser.program
    System.out.println(tree.toStringTree(parser))

    val scope = new Scope(null)
    scope.define(new Value("print", new PrintFunction))
    scope.define(new Value("Array", ArrayObject.constructor))
    val interpreter = new InterpreterVisitor(scope)
    interpreter.visit(tree)

    val main = scope.resolve("main").asInstanceOf[Function]
    main.call(List.empty[SweetObject], List.empty[Value])
  }

  def STDOUT() = { this.output }
}
