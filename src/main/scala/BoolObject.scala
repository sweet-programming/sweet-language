class BoolObject(b: Boolean) extends SweetObject {
  val objectType = "bool"
  val value = b

  override def toString: String = value.toString
  override def equals(other: Any) = {
    other match {
      case that: BoolObject =>
        value == that.value
      case _ => false
    }
  }
}
