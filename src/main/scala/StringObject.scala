class StringObject(s:String) extends SweetObject {
  val value = s
  val objectType = "string"

  override def toString():String = value
  override def add(s:SweetObject):SweetObject = new StringObject(value + s.toString)

  override def equals(other: Any) = {
    other match {
      case that: StringObject =>
        value == that.value
      case _ => false
    }
  }
}
