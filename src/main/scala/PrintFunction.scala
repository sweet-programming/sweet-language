class PrintFunction extends Function(null, null) {
  override def call(args:SweetObject*):SweetObject = {
    Sweet.STDOUT.print(args(0).toString)
    null
  }
}
