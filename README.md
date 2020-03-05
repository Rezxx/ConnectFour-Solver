# Project Title
ConnectFour solver

# Description
This is a program that can predict a best move in ConnectFour game using Alpha-Beta pruning MiniMax algorithm.

# Get started
* Input Argument: 
  1. `row0,row1,row2,row3,row4,row5` indicates starting position, each row with 7 characters could be either `r`,`y` or `.` to represent a red, yellow or empty token.
  2. `red` or `yellow` represents the starting player
  3. For example, if the program is given the following arguments:
  `java ConnectFour .ryyrry,.rryry.,..y.r..,..y....,.......,....... red A 4`. Then this would be built into the game board below, indicating that it is red’s turn to play, and the algorithm should use alpha‐beta pruning with a maximum search depth of 4.
* Output:
- The program will print out the board and suggest the best move for the current player.
- Player should input number between [0, 6] to play.

* Tournament Result: 9th out of 80+ students
![Result](https://github.com/Rezxx/ConnectFour_Solver/blob/master/Result.png)
