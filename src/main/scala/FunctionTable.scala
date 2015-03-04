import java.util.HashMap

class FunctionTable {
    private val table = new HashMap[String, Function]

    set(new PrintFunction())

    def get(name:String):Function = table.get(name)
    def set(function:Function) = table.put(function.name, function)

    class PrintFunction extends Function("print", null, null) {

        override def call(args:Value*):Value = {
            System.out.print(args(0).stringValue)
            null
        }
    }
}
