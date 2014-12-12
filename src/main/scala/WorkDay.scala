import org.joda.time.DateTime

import scala.math.{max, min}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkDay( val date : DateTime, shifts : Traversable[WorkShift]) {

  def hours = shifts.foldLeft(0d)((sum,shift) => sum + shift.hours)

  def eveningHours = shifts.foldLeft(0d)((sum,shift) => sum + shift.eveningHours)

  def overtime = max(0d, hours - 8.0d)

  def salary =
      WorkDay.baseSalary(hours) +
      WorkDay.eveningBonus(eveningHours) +
      WorkDay.overtimeBonus(overtime)
}

object WorkDay {
  private val hourlyWage  = 3.75d
  private val eveningWage = 1.15d

  private def baseSalary(hours: Double) = hours * hourlyWage
  private def eveningBonus(eveningHours: Double) = eveningHours * eveningWage
  private def overtimeBonus(ot: Double) = {
    otBonus25(ot) + otBonus50(max(0d, ot - 2d)) + otBonus100(max(0d, ot - 4d))
  }
  private def otBonus25(ot: Double) = min(2d,ot) * hourlyWage * 0.25d
  private def otBonus50(ot: Double) = min(2d,ot) * hourlyWage * 0.5d
  private def otBonus100(ot: Double) = ot * hourlyWage
}
