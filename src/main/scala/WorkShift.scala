import org.joda.time.{DateTime, Duration}

/**
 * Created by hanzki on 12.12.2014.
 */
class WorkShift(
                 val date : DateTime,
                 val start: DateTime,
                 val end  : DateTime) {

  def length : Double = {
    val d = new Duration(start, end)
    d.getStandardMinutes / 60.0d
  }

}
