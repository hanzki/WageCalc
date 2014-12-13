import org.joda.time.{Interval, Duration, DateTime}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkShift(val interval : Interval) {

  def hours = interval.toDuration.getStandardMinutes / 60.0d

  def eveningHours = Option(WorkShift.nightInterval.overlap(interval)) match {
        case Some(overlap) => overlap.toDuration.getStandardMinutes / 60.0d
        case None => 0d
  }

}

object WorkShift{
  private val nightInterval = new Interval(new DateTime(0,1,1,18,0), Duration.standardHours(12))
}
