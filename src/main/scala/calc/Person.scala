package calc

import org.joda.time.{Interval, YearMonth}

/**
 * Class for representing employee data
 * Created by hanzki on 12.12.2014.
 */
class Person(val name: String,
             val id: Int,
             shifts: List[Interval]) {

  /**
   * Months that this employee has salary from
   */
  val months: List[WorkMonth] =
    shifts.groupBy(i => (i.getStart.getYear, i.getStart.getMonthOfYear))
    .map(t => new WorkMonth(t._2)).toList

  /**
   * @return Sum of salary from all months
   */
  def totalSalary: BigDecimal = months.map(_.salary).sum

  /**
   * Salary from a certain month
   * @param month month which the salary is calculated for
   * @return Salary from specified month or None if employee didn't work during that month
   */
  def monthSalary(month: YearMonth): Option[BigDecimal] =
    months.find(_.month == month) collect {case m => m.salary}

  /**
   * Get string representation of person similar to #toString
   * but with specified month's salary instead of total salary
   * @param month month which the salary is calculated for
   * @return String with employees id, name and month's salary
   */
  def monthString(month: YearMonth): String = f"$id, $name, $$${monthSalary(month).map(_.doubleValue()).getOrElse(0d)}%.2f"

  override def toString: String = f"$id, $name, $$$totalSalary%.2f"

}
