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

  private val thisNight     = new Interval(interval.getStart.withTime(18,0,0,0), Duration.standardHours(12))
  private val previousNight = new Interval(thisNight.getStart.minusDays(1), Duration.standardHours(12))
  private val nextNight     = new Interval(thisNight.getStart.plusDays(1), Duration.standardHours(12))

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
    val thisNightMinutes = Option(thisNight.overlap(interval)) match {
      case Some(ol) => ol.toDuration.getStandardMinutes
      case None => 0
    }
    val previousNightMinutes = Option(previousNight.overlap(interval)) match {
      case Some(ol) => ol.toDuration.getStandardMinutes
      case None => 0
    }
    val nextNightMinutes = Option(nextNight.overlap(interval)) match {
      case Some(ol) => ol.toDuration.getStandardMinutes
      case None => 0
    }

    (thisNightMinutes + previousNightMinutes + nextNightMinutes) / 60.0d
  }

}