class PrintFunction extends Function(null, null) {
  override def call(args: List[SweetObject], parametricArgs: List[Value]): SweetObject = {
    Sweet.STDOUT.print(args(0).toString)
    null
  }
}
