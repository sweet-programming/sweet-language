import java.util.HashMap

class ValueTable {
  private val table = new HashMap[String, Value]

  set(new Value("print", new PrintFunction))

  def get(name:String):SweetObject = table.get(name).content
  def set(value:Value) = table.put(value.name, value)

  class PrintFunction extends Function(null, null) {
    override def call(args:SweetObject*):SweetObject = {
      System.out.print(args(0).asInstanceOf[StringObject].value)
      null
    }
  }
}
