import org.joda.time.{DateTime, Interval}
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by hanzki on 13.12.2014.
 */
class WorkShiftSpec extends FlatSpec with Matchers{

  object Day extends Enumeration {
    type Day = Value
    val Today, Tomorrow = Value
  }
  import Day._

  def makeShift(start: (Int, Day), end: (Int, Day)) = {
    val s = new DateTime(0,1,if(start._2 == Today) 1 else 2, start._1, 0)
    val e = new DateTime(0,1,if(end._2 == Today) 1 else 2, end._1, 0)
    new WorkShift(new Interval(s,e))
  }

  "A shift" should "count hours between 6PM and 6AM as evening hours" in {
    val shiftEarlyEvening         = makeShift((19,Today),(22,Today))
    val shiftEarlyMorning         = makeShift((2,Today),(5,Today))
    val shiftOverlappingStart     = makeShift((16,Today),(21,Today))
    val shiftOverlappingEnd       = makeShift((3,Today),(8,Today))
    val shiftOverlappingMidnight  = makeShift((23,Today),(2,Tomorrow))
    val shiftWholeNight           = makeShift((17,Today),(7,Tomorrow))

    shiftEarlyEvening.eveningHours should be (3d)
    shiftEarlyMorning.eveningHours should be (3d)
    shiftOverlappingStart.eveningHours should be (3d)
    shiftOverlappingEnd.eveningHours should be (3d)
    shiftOverlappingMidnight.eveningHours should be (3d)
    shiftWholeNight.eveningHours should be (12d)
  }

  "A shift" should "throw IllegalArgumentException if the shift is over 24h long" in {
    a [IllegalArgumentException] should be thrownBy {
      makeShift((7, Today), (9, Tomorrow))
    }
  }

}
