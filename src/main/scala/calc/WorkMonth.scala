package calc

import org.joda.time.{Interval, YearMonth}

/**
 * Class representing the work shifts of a single month
 * If the constuctor is passed shifts of multiple months
 * an exception is thrown
 * Created by hanzki on 13.12.2014.
 */
class WorkMonth (intervals: List[Interval]) {
  val month: YearMonth = new YearMonth(intervals.head.getStart)

  if(!intervals.forall(i => new YearMonth(i.getStart) == month))
    throw new IllegalArgumentException("Not all data was from same month")

  val days: List[WorkDay] = intervals.groupBy(_.getStart.getDayOfMonth).map(t => new WorkDay(t._2)).toList

  def salary: Double = days.map(_.salary).sum
}
