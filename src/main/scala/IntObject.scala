class IntObject(i:Int) extends SweetObject {
  val value = i
  val objectType = "int"

  override def add(i:SweetObject):SweetObject = new IntObject(value + i.asInstanceOf[IntObject].value)
  def sub(i:IntObject):IntObject = new IntObject(value - i.value)
  def mul(i:IntObject):IntObject = new IntObject(value * i.value)
  def div(i:IntObject):IntObject = new IntObject(value / i.value)

  override def toString():String = i.toString

  override def equals(other: Any) = {
    other match {
      case that: IntObject =>
        value == that.value
      case _ => false
    }
  }
}
