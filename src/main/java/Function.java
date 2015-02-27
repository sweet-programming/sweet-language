public class Function {
    private String name;
    private InterpreterVisitor visitor;
    private SweetParser.FunctionDefinitionContext definition;

    public Function(String name, InterpreterVisitor visitor, SweetParser.FunctionDefinitionContext definition) {
        this.name = name;
        this.visitor = visitor;
        this.definition = definition;
    }

    public Value call(Value ... args) {
        visitor.visitChildren(definition);
        return null;
    }

    public String getName() {
        return name;
    }
}
