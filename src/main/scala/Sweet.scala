import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._
import java.io.FileInputStream
import java.io.InputStream

object Sweet {
    def main(args: Array[String]) {
        val is = if (args.length > 0) new FileInputStream(args(0)) else System.in

        val input = new ANTLRInputStream(is)
        val lexer = new SweetLexer(input)
        val tokens = new CommonTokenStream(lexer)
        val parser = new SweetParser(tokens)
        val tree = parser.program
        System.out.println(tree.toStringTree(parser))

        val functionTable = new FunctionTable
        val interpreter = new InterpreterVisitor(functionTable)
        interpreter.visit(tree)

        val main = functionTable.get("main")
        main.call()
/*
        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker 
        InterpreterListener interpreter = new InterpreterListener(parser);
        walker.walk(interpreter, tree); // initiate walk of tree with listener
*/
    }
}
