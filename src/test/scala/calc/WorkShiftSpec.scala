package calc

import org.joda.time.{DateTime, Duration, Interval}
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by hanzki on 13.12.2014.
 */
class WorkShiftSpec extends FlatSpec with Matchers{
  import calc.WorkShiftSpec._

  "A shift" should "count hours between 6PM and 6AM as evening hours" in {
    val shiftEarlyEvening         = makeShift(19,3)
    val shiftEarlyMorning         = makeShift(2,3)
    val shiftOverlappingStart     = makeShift(16,5)
    val shiftOverlappingEnd       = makeShift(3,5)
    val shiftOverlappingMidnight  = makeShift(23,3)
    val shiftWholeNight           = makeShift(17,14)
    val shiftTwoNights            = makeShift(23,20)

    shiftEarlyEvening.eveningHours should be (3d)
    shiftEarlyMorning.eveningHours should be (3d)
    shiftOverlappingStart.eveningHours should be (3d)
    shiftOverlappingEnd.eveningHours should be (3d)
    shiftOverlappingMidnight.eveningHours should be (3d)
    shiftWholeNight.eveningHours should be (12d)
    shiftTwoNights.eveningHours should be (8d)
  }

  "A shift" should "throw IllegalArgumentException if the shift is over 24h long" in {
    a [IllegalArgumentException] should be thrownBy {
      makeShift(7, 26)
    }
  }

}

object WorkShiftSpec {

  def makeShift(startHour: Int, durationHour: Int) = {
    val s = new DateTime(0, 1, 1, startHour, 0)
    val d= Duration.standardHours(durationHour)
    new WorkShift(new Interval(s,d))
  }
}
