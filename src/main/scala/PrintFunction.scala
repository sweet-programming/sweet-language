class PrintFunction extends Function(null, null) {
  override def call(args:SweetObject*):SweetObject = {
    System.out.print(args(0).asInstanceOf[StringObject].value)
    null
  }
}
