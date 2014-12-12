/**
 * Created by hanzki on 12.12.2014.
 */
class Person(val name   : String,
             val id     : Int,
             days       : List[WorkDay]) {

  def monthSalary : Double = days.foldLeft(0d)((sum,day) => sum + day.salary)

  override def toString: String = f"$id, $name, $monthSalary%.2f"
}
