WageCalc
========
This is a simple program for parsing work shift data from a CSV file and calculating monthly salaries from the data.

Usage
-------
The main method is located in WageCalc class. It takes a single commandline argument which is the path to CSV file.

Output
---------
The program outputs the month's salary for each employee for each month found from the file.
```
3/2014
------------------------------
1, Janet Java, $705.35
2, Scott Scala, $657.34
3, Larry Lolcode, $377.00

4/2014
------------------------------
1, Janet Java, $0.00
2, Scott Scala, $103.34
3, Larry Lolcode, $97.50
```

CSV format
---------
* Values separated by commas
* First row is header
* One row per work shift
* Five columns (in order):
  1. Name of the employee
  2. Id of the employee
  3. Date in dd.mm.yyyy format
  4. Shift start time in hh:mm format
  5. Shift end time in hh:mm format
* The file can contain shifts from multiple months
* Employee Id should be unique
* Employee name and id should stay constant through the file
```
Person Name,Person ID,Date,Start,End
Scott Scala,2,2.3.2014,6:00,14:00
Janet Java,1,3.3.2014,9:30,17:00
Scott Scala,2,3.3.2014,8:15,16:00
Larry Lolcode,3,3.3.2014,18:00,19:00
Janet Java,1,4.3.2014,9:45,16:30
Scott Scala,2,4.3.2014,8:30,16:30
Janet Java,1,5.3.2014,8:00,16:30
Scott Scala,2,5.3.2014,9:00,17:00
```

Salary calculation
------------------
The salary consists of base salary, evening bonus and overtime bonus. 
The base salary is $3.75 an hour. The evening bonus is an extra $1.15
an hour between 6PM and 6AM. Also if daily hour total is over 8 hours
a overtime bonus is paid. For hours 8<hours<10 it's an extra base salary * 25% an hour.
For hours 10<hours<12 it's an extra base salary * 50% an hour and for hours 
after that it's base salary * 100%.

Known problems
-------------------
* All the monetary variables use Double. A BigDecimal or an Int would be safer 
but because sums are small and this code is not designed for production I think it's okay.
* Error handling in code is lacking and most invalid inputs cause a stack trace to appear.
* There are no checks at the moment for overlapping work shifts of same employee.
* The code could use more tests
