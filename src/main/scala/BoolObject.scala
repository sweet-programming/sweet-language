class BoolObject(b: Boolean) extends SweetObject {
  val value = b
  val objectType = "bool"

  override def toString: String = value.toString

  override def equals(other: Any) = {
    other match {
      case that: BoolObject =>
        value == that.value
      case _ => false
    }
  }
}
