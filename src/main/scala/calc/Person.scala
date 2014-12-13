package calc

import org.joda.time.{Interval, YearMonth}

/**
 * Created by hanzki on 12.12.2014.
 */
class Person(val name: String,
             val id: Int,
             shifts: List[Interval]) {

  val months = shifts.groupBy(i => (i.getStart.getYear, i.getStart.getMonthOfYear)).map(t => new WorkMonth(t._2))

  def totalSalary: Double = months.foldLeft(0d)((sum, month) => sum + month.salary)

  def monthSalary(month: YearMonth) : Option[Double] =
    months.find(_.month == month) collect {case m => m.salary}

  def monthString(month: YearMonth) = f"$id, $name, $$${monthSalary(month).getOrElse(0d)}%.2f"

  override def toString: String = f"$id, $name, $$$totalSalary%.2f"

}
