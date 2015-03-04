import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class Sweet {
    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }

        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        SweetLexer lexer = new SweetLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SweetParser parser = new SweetParser(tokens);
        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));

        FunctionTable functionTable = new FunctionTable();
        InterpreterVisitor interpreter = new InterpreterVisitor(functionTable);
        interpreter.visit(tree);

        Function main = functionTable.get("main");
        main.call();
/*
        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker 
        InterpreterListener interpreter = new InterpreterListener(parser);
        walker.walk(interpreter, tree); // initiate walk of tree with listener
*/
    }
}
