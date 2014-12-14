package calc

import org.joda.time.{Duration, Interval}

/**
 * Class representing a single work shift.
 * If a single work shift is longer than 2h hours
 * an exception is thrown
 * Created by hanzki on 12.12.2014.
 */
class WorkShift(val interval: Interval) {

  if(interval.toDuration.getStandardHours >= 24)
    throw new IllegalArgumentException("Workshifts can't be over 24 hours long")

  private val night = new Interval(interval.getStart.withTime(18,0,0,0), Duration.standardHours(12))
  private val earlyMorning = new Interval(interval.getStart.withTimeAtStartOfDay(), interval.getStart.withTime(6,0,0,0))

  /**
   * The total number of hours worked during the shift. This includes also evening hours.
   * @return total number of hours
   */
  def hours = interval.toDuration.getStandardMinutes / 60.0d

  /**
   * The number of evening hours worked. Evening hours are the time between 6PM and 6AM.
   * @return number of evening hours
   */
  def eveningHours = {
    val atNightMinutes = Option(night.overlap(interval)) match {
      case Some(ol) => ol.toDuration.getStandardMinutes
      case None => 0d
    }
    val inTheMorningMinutes = Option(earlyMorning.overlap(interval)) match {
      case Some(ol) => ol.toDuration.getStandardMinutes
      case None => 0d
    }

    (atNightMinutes + inTheMorningMinutes) / 60.0d
  }

}