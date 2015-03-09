class StringObject(s:String) extends SweetObject {
  val value = s
  val objectType = "string"

  override def toString():String = value
  override def add(s:SweetObject):SweetObject = new StringObject(value + s.toString)
}
