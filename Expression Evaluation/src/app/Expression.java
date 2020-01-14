package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	//iterates through expression input and identifies all variables and arrays
    	//then, takes these variable names and array names, and adds them to their respective ArrayLists
    	//does NOT give them values
    	
    	String t = expr.trim(); //trims any leading or trailing whitespace
    	String variable = "";
    	boolean array = false; // flag to determine array or variable
    	
    	
    	for (int i=0; i<t.length(); i++) { //iterate through string
    		   			
    				if (Character.isLetter(t.charAt(i))){
        				variable += t.charAt(i);
    				}
    				
    				
    				if (t.charAt(i)=='[') {
    					array = true;
    				}
    				
    				
    				
    				//if we have reached a non-letter, time to store variable as appropriate
    				if (!Character.isLetter(t.charAt(i)) && variable!=""){ 
    					
    	    			if (array == false) { //if its not an array
    	        			Variable varname = new Variable(variable);
    	        			if (!vars.contains(varname)){ //it the variable is not already present, add it
    	            			vars.add(varname);  
    	            			System.out.println("variable:" + variable);
    	        			}
    					
    	    			}
    	    			
    	    			if (array == true) {
    	    				Array arr = new Array(variable);
    	        			if (!arrays.contains(arr)){ //it the variable is not already present, add it
    	        				arrays.add(arr); 
    	        				System.out.println("array:" + variable);
    	        			}
    	    				array = false; //array taken care of, reset flag
    	    			}
    	    			
    				variable = ""; //clear variable 
    				}
    				
    			}
    			
    	
		if (variable!=""){ //checks if variable is last term
			
			if (array == false) { //if its not an array
    			Variable varname = new Variable(variable);
    			if (!vars.contains(varname)){ //it the variable is not already present, add it
        			vars.add(varname);  
        			System.out.println("variable:" + variable);
    			}
			
			}
			
			if (array == true) {
				Array arr = new Array(variable);
    			if (!arrays.contains(arr)){ //it the variable is not already present, add it
    				arrays.add(arr); 
    				System.out.println("array:" + variable);
    			}
				array = false; //array taken care of, reset flag
			}
			
		variable = ""; //clear variable 
		}



    		}
    	
    	
  
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
        Stack<Float> numvals = new Stack<Float>();
        Stack<Character> operators = new Stack<Character>(); 
        String exp = expr.trim();
        int i = 0;
        
        boolean array = false;        
        String variable = "";
        
        System.out.println(exp.length());
        //iterate through string and tokenize variables, values, operators and parenthesis, then store into appropriate stacks
        while (i<exp.length()) { //while in bounds
            System.out.println("h2");
            int intval = 0; //integer value
            int tens = 1; //keeps track for large integers
            String numstring = ""; 
            float inttofloat = 0;
            
            char c = exp.charAt(i);
            
            if (Character.isDigit(c)) { //if the character is a digit, stop and tokenize as necc
            
                while (Character.isDigit(exp.charAt(i)) && i<exp.length()) { //while were still in a number
                
                    numstring += exp.charAt(i); //collect whole number, to find value of in another loop
                    i++;
                    if (i>=exp.length()) {
                        break;
                    }
                        
            } 
                
                
                for (int j=numstring.length()-1; j>=0; j--) { //iterate backwards through collected number to get value
                    char d = numstring.charAt(j);
                    int integ = Character.digit(d,10);
                    intval += integ * tens;
                    tens*=10;
                }
                
                inttofloat = (float) intval; //casts the int as a float    
            //System.out.println(numvals.peek());
            System.out.println("h2");
            numvals.push(inttofloat); //pushes numerical float value into values stack
            System.out.println(numvals.peek());
            
            if (exp.matches("[0-9]+")){ //if the expression is only digits (no operators or vars), return the number
                return inttofloat;
            }
            
            
            }
            
            if (i>=exp.length()){
                break;
            }
            
                    
            //if we have reached a letter, determine if array or variable, get value and push into stack
        if (Character.isLetter(exp.charAt(i))){ 
            while (i<exp.length()) {
                if (Character.isLetter(exp.charAt(i))) {
            variable += exp.charAt(i);
            i++;
            System.out.println("i:" + i);
                }
                else {
                    break;
                }
            }
            
            if (i>=exp.length() && variable!=""){ //if the variable is last in the string
                if (array == false) { //if its not an array
                    Variable varname = new Variable(variable);
                    if (vars.contains(varname)){ //it the variable value is stored in the list
                        System.out.println("hh");
                        int ind = vars.indexOf(varname);  //get index of the variable
                        System.out.println("ind: " + ind);
                        float v = (float)vars.get(ind).value; //get the value of the variable
                     //   System.out.println("Expression variable:" + variable);
                     //   System.out.println("v: " + v);
                        numvals.push(v); //pushes variable value into value stack
                      //  System.out.println(numvals.peek());
                     
                    
                    
                    }
                
                }
                
                if (array == true) { //this means we have reached arrayvar[
                    Array arr = new Array(variable);
                    if (arrays.contains(arr)){ //it the array variable is stored, access it
                    /*
                         int aind = arr.indexOf(arr);
                        arrays.add(arr); 
                        System.out.println("array:" + variable); 
                        */
                    }
                    array = false; //array taken care of, reset flag
                }
                
            variable = ""; //clear variable 
                
                
                break; //this is the last element in the string
            }
            
            
            
            if (exp.charAt(i)=='[') {
                array = true;
            }
            
            System.out.println("here");
            
            //if we have reached a non-letter, time identify variable value and store into value stack
            if (!Character.isLetter(exp.charAt(i)) && variable!=""){ 
                
                if (array == false) { //if its not an array
                    Variable varname = new Variable(variable);
                    if (vars.contains(varname)){ //it the variable value is stored in the list
                        System.out.println("hh");
                        int ind = vars.indexOf(varname);  //get index of the variable
                        float v = (float)vars.get(ind).value; //get the value of the variable
                        System.out.println("v:" + v);
                        System.out.println("Expression variable:" + variable);
                        numvals.push(v);
                        System.out.println(numvals.peek());
                    }
                    
                    
                    
                
                }
                
                if (array == true) { //this means we have reached arrayvar[
                    Array arr = new Array(variable);
                    if (arrays.contains(arr)){ //it the array variable is stored, access it
                    /*
                         int aind = arr.indexOf(arr);
                        arrays.add(arr); 
                        System.out.println("array:" + variable); 
                        */
                    }
                    array = false; //array taken care of, reset flag
                }
                
            variable = ""; //clear variable 
            }
            
        }
        
        if ((exp.charAt(i)=='+') || (exp.charAt(i)=='-') || (exp.charAt(i)=='/') ||(exp.charAt(i)=='*') ||(exp.charAt(i)=='(') ||(exp.charAt(i)==')')) {
            System.out.println("HERE");
            if (exp.charAt(i)==')') {
            	while (operators.peek()!='(') {
            		float first = numvals.pop();
            		if (numvals.isEmpty()) {
            			numvals.push(first); //if there is only one value, push back in - nothing to eval
            			break;
            		}
            		else {
            			numvals.push(first);
            			if ((operators.peek()=='(') || ((exp.charAt(i)=='/') || (exp.charAt(i)=='*')) && ((operators.peek()=='+') || (operators.peek()=='-'))){
            			    
                            operators.push(exp.charAt(i));
                            }
                        else if (exp.charAt(i)==')') {
                            while (operators.peek()!='(') {
                                float two2 = numvals.pop();
                                float one1 = numvals.pop();
                                System.out.println("else if, )" + operators.peek());
                                char op = operators.pop();
                                if  (op == '+') {
                                    numvals.push(one1 + two2);
                                }
                                else if  (op == '-') {
                                    numvals.push(one1 - two2);
                                }
                                else if  (op == '*') {
                                    numvals.push(one1 * two2);
                                }
                                else {//  (op == '+') {
                                    numvals.push(one1 / two2);
                                }
                                
                                System.out.println("opo" + operators.peek());
                                System.out.println("numba" + numvals.peek());
                                if (operators.peek()=='(') {
                                	operators.pop();
                                	break;
                                }
                                if (operators.isEmpty()) {
                                	break;
                                }
                            }
                            
                            //float temp = operators.pop();//temporar
                            if (!operators.isEmpty()) {
                                
                                operators.pop(); //pops left parenthesis
                            }
   
                        }
                        else { //top of stack has higher precedence, operate on

                            float two = numvals.pop();

                            float one = numvals.pop();

                            //System.out.println("operators: " + operators.peek());
                            if (operators.peek()=='+') {
                                operators.pop();
                                numvals.push(one + two);
                            } 
                            else if (operators.peek()=='-') {
                                operators.pop();
                                numvals.push(one - two);
                            } 
                            else if (operators.peek()=='*') {

                                operators.pop();
                                numvals.push(one * two);
                            } 
                            else { //(operators.peek()=='/') {
                                operators.pop();
                                numvals.push(one / two);
                            } 

                            operators.push(exp.charAt(i)); // push lesser precedent operator into stack
                        
                        }
            		}
            	if (operators.isEmpty()) {
            		break;
            	}
            	if (operators.peek()==')') {
            		operators.pop();
            		break;
            	}
            	
            	
            	}
            }
            
            if (operators.isEmpty() || (exp.charAt(i)=='(')) {
            operators.push(exp.charAt(i));

            }
            //if current char has higher precedence than top of stack
            else if ((operators.peek()=='(') || ((exp.charAt(i)=='/') || (exp.charAt(i)=='*')) && ((operators.peek()=='+') || (operators.peek()=='-'))){
    
                operators.push(exp.charAt(i));
                }
            else if (exp.charAt(i)==')') {
                while (operators.peek()!='(') {
                    float two2 = numvals.pop();
                    float one1 = numvals.pop();
                    System.out.println("else if, )" + operators.peek());
                    char op = operators.pop();
                    if  (op == '+') {
                        numvals.push(one1 + two2);
                    }
                    else if  (op == '-') {
                        numvals.push(one1 - two2);
                    }
                    else if  (op == '*') {
                        numvals.push(one1 * two2);
                    }
                    else {//  (op == '+') {
                        numvals.push(one1 / two2);
                    }
                    
                    System.out.println("opo" + operators.peek());
                    System.out.println("numba" + numvals.peek());
                }
                
                //float temp = operators.pop();//temporar
                operators.pop(); //pops left parenthesis
            }
            else { //top of stack has higher precedence, operate on

                float two = numvals.pop();

                float one = numvals.pop();

                //System.out.println("operators: " + operators.peek());
                if (operators.peek()=='+') {
                    operators.pop();
                    numvals.push(one + two);
                } 
                else if (operators.peek()=='-') {
                    operators.pop();
                    numvals.push(one - two);
                } 
                else if (operators.peek()=='*') {

                    operators.pop();
                    numvals.push(one * two);
                } 
                else { //(operators.peek()=='/') {
                    operators.pop();
                    numvals.push(one / two);
                } 

                operators.push(exp.charAt(i)); // push lesser precedent operator into stack
            
            }

                    
                    
        
            }

        
        i++;
            }
        
            
        
        
        while (!operators.isEmpty()) {
        	if (operators.peek()==')') {
        		operators.pop();
        	}
            System.out.println("here1");
           // System.out.println("operator: " + operators.peek());
          //  System.out.println("number: " + numvals.peek());

            
            
         float two = numvals.pop();
         System.out.println("here2");
      //  System.out.println("number: " + numvals.peek());
        //if (numvals.size()==0) {
        //    break;
        //}
        if (numvals.isEmpty()) {
        	numvals.push(two);
        	break;
        }
        if (operators.isEmpty()) {
        	break;
        }
        System.out.println("here3");
        float one = numvals.pop();
       System.out.println("operator: " + operators.peek());
       System.out.println(one + two);
       //System.out.println("number: " + numvals.peek());
        if (operators.peek()=='+') {
            numvals.push(one + two);
            operators.pop();
        } 
        else if (operators.peek()=='-') {
            numvals.push(one - two);
            operators.pop();
        } 
        else if (operators.peek()=='*') {
            //System.out.println("operator: " + operators.peek());
            numvals.push(one * two);
            operators.pop();
        } 
        else { //(operators.peek()=='/') {
            numvals.push(one / two);
            operators.pop();
        } 
    //    System.out.println("numval " + numvals.peek());
        }
        
        

        
        return numvals.peek();
        //return 0;
        // numvals.pop();
    }
}
