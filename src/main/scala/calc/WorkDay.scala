package calc

import org.joda.time.{Interval, LocalDate}

import scala.math.{max, min}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkDay(intervals: List[Interval]) {
  val day = new LocalDate(intervals.head.getStart)

  if(!intervals.forall(i => day == new LocalDate(i.getStart)))
    throw new IllegalArgumentException("Not all data was from same day")

  val shifts = intervals.map(new WorkShift(_))

  def hours = shifts.foldLeft(0d)((sum, shift) => sum + shift.hours)

  def eveningHours = shifts.foldLeft(0d)((sum, shift) => sum + shift.eveningHours)

  def overtime = max(0d, hours - 8.0d)

  def salary =
    WorkDay.baseSalary(hours) +
      WorkDay.eveningBonus(eveningHours) +
      WorkDay.overtimeBonus(overtime)
}

object WorkDay {
  val hourlyWage = 3.75d
  val eveningWage = 1.15d
  val overtimeBonus1 = hourlyWage * 0.25d
  val overtimeBonus2 = hourlyWage * 0.50d
  val overtimeBonus3 = hourlyWage * 1.00d

  private def baseSalary(hours: Double) = hours * hourlyWage

  private def eveningBonus(eveningHours: Double) = eveningHours * eveningWage

  private def overtimeBonus(ot: Double) = {
    otBonus25(ot) + otBonus50(max(0d, ot - 2d)) + otBonus100(max(0d, ot - 4d))
  }

  private def otBonus25(ot: Double) = min(2d, ot) * overtimeBonus1

  private def otBonus50(ot: Double) = min(2d, ot)  * overtimeBonus2

  private def otBonus100(ot: Double) = ot  * overtimeBonus3
}
