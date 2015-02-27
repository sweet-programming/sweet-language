import org.antlr.v4.runtime.misc.NotNull;
import java.util.List;
import java.util.ArrayList;

public class InterpreterVisitor extends SweetBaseVisitor<InterpreterContext> {
    private FunctionTable functionTable;
    private InterpreterContext context = new InterpreterContext();

    public InterpreterVisitor(FunctionTable functionTable) {
        this.functionTable = functionTable;
    }

    @Override
	public InterpreterContext visitProgram(@NotNull SweetParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

	@Override
    public InterpreterContext visitAssign(@NotNull SweetParser.AssignContext ctx) {
        SweetParser.FunctionDefinitionContext functionDefinition = ctx.functionDefinition();
        if (functionDefinition != null) {
            String id = ctx.ID().toString();
            functionTable.set(new Function(id, this, functionDefinition));
        }
        return visitChildren(ctx);
    }

    @Override
	public InterpreterContext visitFunctionDefinition(@NotNull SweetParser.FunctionDefinitionContext ctx) {
        return context;
    }

	@Override
    public InterpreterContext visitStatement(@NotNull SweetParser.StatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public InterpreterContext visitFunctionCall(SweetParser.FunctionCallContext ctx) {
        List<Value> values = new ArrayList<Value>();
        for (SweetParser.FormulaContext formula: ctx.formula()) {
            values.add(new StringValue(removeQuotes(formula.STRING().getText())));
        }
        functionTable.get(ctx.ID().toString()).call(values.toArray(new Value[0]));
        return context;
    }
    
    private static String removeQuotes(String str) {
    	return str.substring(1, str.length() - 1);
    }

}
