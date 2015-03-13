import scala.collection.mutable.Map

class Scope(p:Scope) {
  val parent = p
  val valueTable = Map.empty[String, Value]

  def resolve(name:String):SweetObject = {
    if (!isDefined(name)) {
      if (parent == null) {
        sys.error(s"undefined value: '$name'")
      }
       return  parent.resolve(name)
    }

    valueTable(name).content
  }

  def define(value:Value): Unit = {
    val name = value.name
    if (isDefined(name))
      sys.error(s"can't define defined value: '$name'")

    valueTable += name -> value
  }

  def declare(name: String): Unit = {
    valueTable += name -> null
  }

  def isDefined(name: String): Boolean = {
    valueTable.contains(name) && valueTable(name) != null
  }
}
