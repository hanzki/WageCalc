package calc

import org.joda.time.{Duration, Interval}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkShift(val interval: Interval) {

  if(interval.toDuration.getStandardHours >= 24)
    throw new IllegalArgumentException("Workshifts can't be over 24 hours long")

  private val night = new Interval(interval.getStart.withTime(18,0,0,0), Duration.standardHours(12))
  private val earlyMorning = new Interval(interval.getStart.withTimeAtStartOfDay(), interval.getStart.withTime(6,0,0,0))

  def hours = interval.toDuration.getStandardMinutes / 60.0d

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