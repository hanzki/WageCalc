package calc

import org.joda.time.{Interval, YearMonth}

/**
 * Created by hanzki on 13.12.2014.
 */
class WorkMonth (intervals: List[Interval]) {
  val month = new YearMonth(intervals.head.getStart)

  if(!intervals.forall(i => month == new YearMonth(i.getStart)))
    throw new IllegalArgumentException("Not all data was from same month")

  val days = intervals.groupBy(_.getStart.getDayOfMonth).map(t => new WorkDay(t._2))

  def salary : Double = days.foldLeft(0d)((sum, day) => sum + day.salary)
}
