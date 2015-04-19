import org.scalatest.FlatSpec
import java.io._
import scala.collection.JavaConversions._
import scala.io.Source

class SetSpec extends FlatSpec {
  for (file <- new File("samples").listFiles.filter(_.getName endsWith ".sweet")) {
    val resultFile = new File(s"samples/results/${file.getName.replaceAll("\\.sweet$", "")}.result")
    s"Result execute script ${file.getName}" should s"same as ${resultFile.getName}" in {
      val os = new ByteArrayOutputStream();
      Sweet.run(new FileInputStream(file), new PrintStream(os))
      val result = os.toString("UTF8");

      val expected = Source.fromFile(resultFile).getLines.reduce((x, y) => s"${x}\n${y}")

      assert(result == expected)
    }
  }
}
