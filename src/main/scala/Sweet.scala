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

        val valueTable = new ValueTable
        val interpreter = new InterpreterVisitor(valueTable)
        interpreter.visit(tree)

        val main = valueTable.get("main").asInstanceOf[Function]
        main.call()
/*
        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker 
        InterpreterListener interpreter = new InterpreterListener(parser);
        walker.walk(interpreter, tree); // initiate walk of tree with listener
*/
    }
}
