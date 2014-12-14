package calc

import org.joda.time.{Interval, LocalDate}

import scala.math.{max, min}

/**
 * Class representing the work shifts of a single day. Most of the
 * salary calculation logic is in this class.
 * All shifts passed to the constructor have to be from the same day
 * or an exception is thrown
 * Created by hanzki on 12.12.2014.
 */
class WorkDay(intervals: List[Interval]) {
  val day: LocalDate = new LocalDate(intervals.head.getStart)

  if(!intervals.forall(i => day == new LocalDate(i.getStart)))
    throw new IllegalArgumentException("Not all data was from same day")

  val shifts: List[WorkShift] = intervals.map(new WorkShift(_))

  /**
   * Number of all work hours during this day including evenings and overtime
   * @return hour total this day
   */
  def hours: Double = shifts.map(_.hours).sum

  /**
   * @return number of hours that earn evening bonus
   */
  def eveningHours: Double = shifts.map(_.eveningHours).sum

  /**
   * Work time that goes over 8 hours per day are counted as overtime
   * @return number of hours that are counted as overtime
   */
  def overtime: Double = max(0d, hours - 8.0d)

  /**
   * Total salary from this day including all bonuses
   * @return total ammount of salary
   */
  def salary: Double =
    WorkDay.baseSalary(hours) +
    WorkDay.eveningBonus(eveningHours) +
    WorkDay.overtimeBonus(hours)
}

object WorkDay {
  val hourlyWage = 3.75d
  val eveningWage = 1.15d
  val overtimeBonus1 = hourlyWage * 0.25d
  val overtimeBonus2 = hourlyWage * 0.50d
  val overtimeBonus3 = hourlyWage * 1.00d

  /**
   * Base salary includes only normal hourly wage for each hour worked
   * @param hours total number of hours
   * @return ammount of salary
   */
  private def baseSalary(hours: Double) = hours * hourlyWage

  /**
   * The evening bonus is counted on top of base salary for hours between 6PM and 6AM
   * @param eveningHours number of hours during evening bonus
   * @return ammount of evening bonus not including the base salary
   */
  private def eveningBonus(eveningHours: Double) = eveningHours * eveningWage

  /**
   * The overtime bonus is calculated for each hour over 8 each day
   * @param hours total number of hours
   * @return ammount of overtime bonus not including base salary or evening bonus
   */
  private def overtimeBonus(hours: Double) = {
    otBonus25(hours) + otBonus50(hours) + otBonus100(hours)
  }

  /**
   * Calculates overtime bonus for the first 2 hours after normal 8 hours.
   * During this time the bonus equals to 25% of normal hourly wage.
   * @param hours total number of hours
   * @return ammount of overtime bonus
   */
  private def otBonus25(hours: Double) = if(hours <= 8d) 0d else min(2d, hours-8d) * overtimeBonus1

  /**
   * Calculates overtime bonus for the first 2 hours after 10 hours.
   * During this time the bonus equals to 50% of normal hourly wage.
   * @param hours total number of hours
   * @return ammount of overtime bonus
   */
  private def otBonus50(hours: Double) = if(hours <= 10d) 0d else min(2d, hours-10d) * overtimeBonus2

  /**
   * Calculates overtime bonus for the time worked after 12 hours.
   * During this time the bonus equals to 100% of normal hourly wage.
   * @param hours total number of hours
   * @return ammount of overtime bonus
   */
  private def otBonus100(hours: Double) = if(hours <= 12d) 0d else (hours-12d) * overtimeBonus3
}
