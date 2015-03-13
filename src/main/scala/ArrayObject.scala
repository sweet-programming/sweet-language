class ArrayObject(s: Int) extends SweetObject {
  val size = s
  val objectType = "array"
  val array = new Array[SweetObject](s)

  def get(i: Int): SweetObject = array(i)
  def set(i: Int, v: SweetObject): Unit = array(i) = v

  def isDefined(i: Int): Boolean = array(i) != null
}

object ArrayObject extends ArrayObject(0) {
  def constructor(): Function = {
    new Function(null, null) {
      override def call(args: SweetObject*): SweetObject = new ArrayObject(args(0).asInstanceOf[IntObject].value)
    }
  }
}
