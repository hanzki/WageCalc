import org.joda.time.{DateTime, Interval}

/**
 * Created by hanzki on 12.12.2014.
 */
class Person(val name   : String,
             val id     : Int,
             days       : Traversable[WorkDay]) {

  def this(name : String, id : Int, shifts: List[(DateTime, Interval)]) = {
    this(name, id, shifts.groupBy(_._1).map{case (k,v)=> new WorkDay(k,v.map(_._2))})
  }

  def monthSalary : Double = days.foldLeft(0d)((sum,day) => sum + day.salary)

  override def toString: String = f"$id, $name, $$$monthSalary%.2f"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Person]

  override def equals(other: Any): Boolean = other match {
    case that: Person =>
      (that canEqual this) &&
        name == that.name &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
