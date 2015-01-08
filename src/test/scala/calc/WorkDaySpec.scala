package calc

import org.joda.time.{Interval, Duration, DateTime}
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by hanzki on 14.12.2014.
 */
class WorkDaySpec extends FlatSpec with Matchers{
  "A day" should "calculate base salary correctly" in {
    val shiftD8 = WorkShiftSpec.makeShift(9, 8).interval
    val shiftD2 = WorkShiftSpec.makeShift(7, 2).interval

    val day8hSalary = new WorkDay(List(shiftD8)).salary
    val day2hSalary = new WorkDay(List(shiftD2)).salary
    val day4hSalary = new WorkDay(List(shiftD2, shiftD2)).salary

    day8hSalary should be (WorkDay.hourlyWage * 8d)
    day2hSalary should be (WorkDay.hourlyWage * 2d)
    day4hSalary should be (WorkDay.hourlyWage * 4d)
  }

  "A day" should "calculate salary with evening bonus correctly" in {
    val shiftD2 = WorkShiftSpec.makeShift(7, 2).interval
    val shiftE4 = WorkShiftSpec.makeShift(19, 4).interval

    val day4h4eSalary = new WorkDay(List(shiftE4)).salary
    val day6h4eSalary = new WorkDay(List(shiftD2,shiftE4)).salary

    day4h4eSalary should be (WorkDay.hourlyWage * 4d + WorkDay.eveningWage * 4d)
    day6h4eSalary should be (WorkDay.hourlyWage * 6d + WorkDay.eveningWage * 4d)
  }

  "A day" should "calculate salary with overtime correctly" in {
    val shiftD8 = WorkShiftSpec.makeShift(9, 8).interval
    val shiftD2 = WorkShiftSpec.makeShift(7, 2).interval

    val day10h2otSalary = new WorkDay(List(shiftD2,shiftD8)).salary
    val day12h4otSalary = new WorkDay(List(shiftD2,shiftD2,shiftD8)).salary
    val day16h8otSalary = new WorkDay(List(shiftD8,shiftD8)).salary

    day10h2otSalary should be (WorkDay.hourlyWage * 10d + 2d * WorkDay.overtimeBonus1)
    day12h4otSalary should be (
      12d * WorkDay.hourlyWage
        + 2d * WorkDay.overtimeBonus1
        + 2d * WorkDay.overtimeBonus2)
    day16h8otSalary should be (
      16d * WorkDay.hourlyWage
        + 2d * WorkDay.overtimeBonus1
        + 2d * WorkDay.overtimeBonus2
        + 4d * WorkDay.overtimeBonus3)
  }

  "A day" should "calculate salary with overtime and evening bonus correctly" in {
    val shiftD8 = WorkShiftSpec.makeShift(9, 8).interval
    val shiftD2 = WorkShiftSpec.makeShift(7, 2).interval
    val shiftE4 = WorkShiftSpec.makeShift(19, 4).interval

    val day14h4e6otSalary = new WorkDay(List(shiftD2,shiftD8,shiftE4)).salary

    day14h4e6otSalary should be (
      14d * WorkDay.hourlyWage
        + 4d * WorkDay.eveningWage
        + 2d * WorkDay.overtimeBonus1
        + 2d * WorkDay.overtimeBonus2
        + 2d * WorkDay.overtimeBonus3)
  }

  "A day" should "throw IllegalArgumentException if all shifts aren't from the same day" in {
    a [IllegalArgumentException] should be thrownBy {
      new WorkDay(List(
                      new Interval(new DateTime(0,1,1,7,0,0), Duration.standardHours(3) ),
                      new Interval(new DateTime(0,1,2,8,0,0), Duration.standardHours(2))
                  ))
    }
  }
}
