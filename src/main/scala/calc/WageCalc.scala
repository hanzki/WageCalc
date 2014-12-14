package calc

import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import scala.io.Source

/**
 * This class parses workshift data from a CSV file and displays monthly salaries of employees
 * Created by hanzki on 12.12.2014.
 */
object WageCalc {

  private val DT_FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy/HH:mm")

  /**
   * Loads a csv file indicated by the file path and splits all lines and commas in it.
   * @param fullPath path to the CSV file
   * @return A list of CSV rows as arrays of Strings
   */
  def parseCSV(fullPath: String): List[Array[String]] = {
    val lines = Source.fromFile(fullPath).getLines().toList
    val linesWithoutHeaders = lines.tail //
    linesWithoutHeaders.map(_.split(','))
  }

  /**
   * Parses the strings of one CSV row to correct objects.
   * The input needs to contain following parts in this order:
   * 0: Name of the employee
   * 1: Id of the employee
   * 2: Date in format "dd.mm.yyyy"
   * 3: Shift start time in format "hh:mm"
   * 4: Shift end time in format "hh:mm"
   * @param line Array containing specified strings
   * @return Tuple with (Employee name, Employee Id, Work shift interval)
   */
  def parseCSVLine(line: Array[String]) : (String, Int, Interval) = {
    val start = DT_FORMAT.parseDateTime(line(2).trim + '/' + line(3).trim)
    val end = DT_FORMAT.parseDateTime(line(2).trim + '/' + line(4).trim)
    (
      line(0).trim,
      line(1).trim.toInt,
      if(start.isBefore(end)) new Interval(start,end)
      else new Interval(start,end.plusDays(1))
    )
  }

  /**
   * Prints every person's salary for every month that someone has salary on
   * @param persons list of persons
   */
  def printSalaryOutput(persons: List[Person]) {
    val months = persons.flatMap(_.months.map(_.month)) //list of all months that someone has salary on
                        .toList.distinct //remove duplicates
                        .sortWith((m1,m2)=>m1.isBefore(m2)) //sort by date

    months.map(m => //for each month
      persons.map(_.monthString(m)) //get output lines for each person
        .mkString(s"${m.getMonthOfYear}/${m.getYear}\n${"-"*30}\n","\n","\n") //separate months
    ).foreach(println(_))
  }

  /**
   * Entry point to wage calculation application
   * @param args First argument needs to be the full path of CSV file
   */
  def main(args: Array[String]) {
    if(args.length < 1) Console.err.println("Usage: <path to CSV file with work shift information>")
    else
    {
      val data = parseCSV(args(0)).filter(_.length == 5).map(parseCSVLine)
      val groupedByNameAndId = data.groupBy(d => (d._1, d._2))

      val persons = groupedByNameAndId.map{
        case (nameId,listOfValues) =>
          new Person( name = nameId._1,
                      id = nameId._2,
                      shifts = listOfValues.map(_._3))
      }.toList.sortBy(_.id)

      printSalaryOutput(persons)
    }
  }
}
