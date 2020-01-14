# Data-Structures-Projects
Completed in undergrad

### Expression Evaluation
A program which reads a mathematical expression from the user which may contain numbers, scalar variables, arrays and any of the following operators: + - * / ( ) [ ]
- The expression may be nested using parenthesis and brackets to any degree
- Any variables and arrays must be predefined in a .txt file in order to be correctly read (See the .txt files for examples)

Files
Array.java - Holds a (name, array of integer values) pair for an array. The name is a sequence of one or more letters.   
Evaluator.java - Contains the main class, which reads inputs from user and .txt files using scanner  
Variable.java - Holds a (name, integer value) pair for a simple (non-array) variable.  
Expression.java - Contains the bulk of the work.  
Methods:  
makeVariableLists() - iterates through expression input and identifies all variables and arrays. Next, it takes these variable names and array names, and adds them to their respective ArrayLists
loadVariableValues() - Loads values for variables and arrays in the expression from .txt file
evaulate() - Evauluates the expression using stack data structures for appropriate operation order

### Friends
Given a .txt file which contains names, corresponding school and connected "friends" to represent a graph, program can return a number of things including: shortest path between two friends, groups of "cliques" and friends which serve as connectors between two cliques.


Files
Graph.java - This file holds the Graph utilizing a hash map
Friends.java - This file contains the bulk of the work. 
Methods: 
shortestChain() -  finds the shortest chain of people from p1 to p2 using breadth first search (BFS). Chain is returned as a sequence of names starting with p1, and ending with p2. Each pair (n1,n2) of consecutive names in the returned chain is an edge in the graph.
cliques() - Recursively finds all cliques of students in a given school. Returns an array list of array lists - each constituent array list contains the names of all students in a clique.
connectors() - Recursively finds and returns all connectors in a graph.


### Trie
Creates a trie using any dictionary of words input by the user

### Big Integer
Overcomes the maximum number of digits that a java program can store by implementing linked lists to store digits
