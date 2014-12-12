/**
 * Created by hanzki on 12.12.2014.
 */
class Person(val name   : String,
             val id     : Int,
             val shifts : List[WorkShift]) {

  def totalSalary : Double = {
    0d
  }
  override def toString: String = f"$id, $name, $totalSalary%.2f"
}
