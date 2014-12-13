import org.joda.time.{Interval, DateTime}

import scala.io.Source

/**
 * Created by hanzki on 12.12.2014.
 */
object WageCalc {

  private val HEADERS = List("Person Name","Person ID","Date","Start","End")
  //Scott Scala, 2, 2.3.2014, 6:00, 14:00
  def parseCSV(fullPath: String): List[Array[String]] = {
    val lines = Source.fromFile(fullPath).getLines().toList
    val data = lines.tail
    data.map(_.split(','))
  }

  def parseValues(data: List[Array[String]]) = {
    data.map(line => (line(0), line(1).toInt, parseDate(line(2)), parseInterval(line(3), line(4))))
  }

  def parseDate(date: String) : DateTime ={
    val dmy = date.split('.').map(_.toInt)
    new DateTime(dmy(2),dmy(1),dmy(0),0,0)
  }

  def parseInterval(from: String, to: String) : Interval ={
    val fromInt = from.split(':').map(_.toInt)
    val toInt = to.split(':').map(_.toInt)
    val start = new DateTime(0,1,1,fromInt(0),fromInt(1))
    if(toInt(0) > fromInt(0) || (toInt(0) == fromInt(0) && toInt(1) > fromInt(1)))
        new Interval(start, new DateTime(0,1,1,toInt(0),toInt(1)))
    else
        new Interval(start, new DateTime(0,1,2,toInt(0),toInt(1)))

  }

  def main(args: Array[String]) {
    val strData = parseCSV(args(0))
    val parsedData = parseValues(strData)
    val persons = parsedData.groupBy(d => (d._1,d._2)).map{case (k,data) => new Person(k._1,k._2,data.map(r=>(r._3,r._4)))}

    persons.toList.sortBy(_.id).foreach(println(_))
    //data.foreach(x => println(x.toList))
  }
}
