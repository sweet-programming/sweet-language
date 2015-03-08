class Value(n:String, o:SweetObject) {
  val name = n
  val content = o

  override def toString:String = { s"$name: $content" }
}
