import org.joda.time.{Interval, Duration, DateTime}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkShift(val start: DateTime, val end: DateTime) {

  private val shiftInterval = new Interval(start, end)
  private val nightInterval = new Interval(start.withTime(18,0,0,0), Duration.standardHours(12))

  def hours = shiftInterval.toDuration.getStandardMinutes / 60.0d

  def eveningHours = Option(nightInterval.overlap(shiftInterval)) match {
        case Some(interval) => interval.toDuration.getStandardMinutes / 60.0d
        case None => 0d
    }

}
