import scala.collection.mutable.Map

class Scope(p:Scope) {
  val parent = p
  val valueTable = Map.empty[String, Value]

  def resolve(name:String):SweetObject = {
    if (!valueTable.contains(name)) {
      if (parent == null) {
        sys.error(s"undefined value: '$name'")
      }
       return  parent.resolve(name)
    }

    valueTable(name).content
  }

  def define(value:Value) = {
    valueTable += value.name -> value
  }
}
