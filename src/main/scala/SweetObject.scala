trait SweetObject {
  val objectType:String

  override def toString: String = s"Object(type: $objectType)"
  def add(o:SweetObject):SweetObject = sys.error(s"Don't use '+' for $objectType'")
}
