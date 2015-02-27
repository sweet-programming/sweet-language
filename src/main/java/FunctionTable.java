import java.util.Map;
import java.util.HashMap;

public class FunctionTable {
    private Map<String, Function> table = new HashMap<String, Function>();

    public FunctionTable() {
        set(new PrintFunction());
    }

    public Function get(String name) {
        return table.get(name);
    }

    public void set(Function function) {
        table.put(function.getName(), function);
    }

    private class PrintFunction extends Function {
        public PrintFunction() {
            super("print", null, null);
        }

        @Override
        public Value call(Value ... args) {
            System.out.print(args[0].toString());
            return null;
        }
    }
}
