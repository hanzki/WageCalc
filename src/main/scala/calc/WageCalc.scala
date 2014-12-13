package calc

import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import scala.io.Source

/**
 * Created by hanzki on 12.12.2014.
 */
object WageCalc {

  private val DT_FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy/HH:mm")

  //Scott Scala, 2, 2.3.2014, 6:00, 14:00
  def parseCSV(fullPath: String): List[Array[String]] = {
    val lines = Source.fromFile(fullPath).getLines().toList
    val data = lines.tail
    data.map(_.split(','))
  }

  def parseCSVLine(line: Array[String]) = {
    val start = DT_FORMAT.parseDateTime(line(2) + '/' + line(3))
    val end = DT_FORMAT.parseDateTime(line(2) + '/' + line(4))
    (
      line(0),
      line(1).toInt,
      if(start.isBefore(end)) new Interval(start,end)
      else new Interval(start,end.plusDays(1))
    )
  }

  def main(args: Array[String]) {
    if(args.length < 1) Console.err.println("Wrong number of arguments")
    else
    {
      val data = parseCSV(args(0)).map(parseCSVLine)

      val persons = data.groupBy(d => (d._1, d._2))
                    .map(t => new Person(t._1._1, t._1._2, t._2.map(_._3)))
                    .toList.sortBy(_.id)

      val months = persons.flatMap(_.months.map(_.month)).toList.distinct.sortWith((m1,m2)=>m1.isBefore(m2))

      months.map(m =>
                  persons.map(_.monthString(m))
                  .mkString(s"${m.getMonthOfYear}/${m.getYear}\n---------------\n","\n","\n")
                ).foreach(println(_))
      //persons.toList.sortBy(_.id).foreach(println(_))
    }
  }
}
