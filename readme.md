
Calculator with Exchange Currency

Project Description
It is an app where you can run many mathematical operations such as (+, -, *, /, %) with the proper 
operation priority. First (*, /, %) and then (+, -), from left to right. You can replace operators 
or decimal with each other. Erase previous numbers or operators with backspace or reset with AC.
If you divide or mod with zero the app send to user a message that hw can not divide with zero and 
reset the calculator.
You also have one more option to change the calculator with exchange currency pressing euro symbol.
The exchange has live action and you can change currencies from the spinners dropdown.

Implementation
For the implementation i used an abstract class of Symbols with 2 subclasses (Number, Operator). 
Number is a subclass with a value the "number": Big decimal. Operators is a sealed class you has 
companion object all the operations the user choose with the buttons. Every operator(object) has a 
different action. 

Installation
To install the app you have to build .apk file from android studio.