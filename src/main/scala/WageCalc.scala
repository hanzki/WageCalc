import scala.io.Source

/**
 * Created by hanzki on 12.12.2014.
 */
object WageCalc {

  private val HEADERS = List("Person Name","Person ID","Date","Start","End")

  def parseCSV(fullPath: String): Unit = {
    val lines = Source.fromFile(fullPath).getLines().toList
    val headers = lines.head
  }

  def main(args: Array[String]) {
    println("TODO: Everything")
  }
}
