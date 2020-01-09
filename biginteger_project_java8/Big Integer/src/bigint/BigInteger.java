package bigint;


/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		
		BigInteger bi = new BigInteger(); //creates new BigInteger object
		
		
		/* IMPLEMENT THIS METHOD */
		String integ = integer.trim(); //trims leading and trailing whitespace from integer
		
		if (integ.equals("0")){ //if zero, return empty big integer list
			return bi;
		}
		
		DigitNode first = null; //creates two pointers
		DigitNode last = null;
		
		
		//removes leading zeros and takes care of any positive or negative signs
		int z = 0;
		while ((integ.charAt(z)==('-'))||(integ.charAt(z)==('+'))||(integ.charAt(z)==('0'))){
			if (integ.charAt(z) == '+') { //ignore positive sign
				z++;
				continue;
			}
			if (integ.charAt(z) == '-') { //do not parse negative sign, but change boolean negative to true for big integer object
				bi.negative = true;
				z++;
				continue;
			}			
		z++;
		
		if (z>integ.length()-1) { //takes care of case where entire string is a combo of only zeros/positive/negative signs etc
			break;
		}
		
		
		}

		 
		// iterates through string backwards, and stops where 000s end
		for (int i = integ.length()-1; i>=z; i--){
			
			char b = integ.charAt(i);
						
			if (b == ' ') { //ignore positive sign
				throw new IllegalArgumentException();
			}
			
			
			if (!((b == '0') || (b == '1')|| (b == '2')|| (b == '3')|| 
			(b == '4')|| (b == '5')|| (b == '6')|| (b == '7')|| (b == '8')|| (b == '9')))    { //deals with leading zeros, need to finish this
				throw new IllegalArgumentException();
			}
			
			
			bi.numDigits++; //increment number of digits by 1
			
			
			int a = b - '0'; // converts char to integer
			
			DigitNode pointer = new DigitNode(a, null); //creates node with integer info
			
			if (first==null) { //if this is the first node, point first pointer to it
			first = pointer;
			//first = last;
			}
			else{ //not first node, connect previous node to this "pointer" node
			last.next = pointer;
			}
			last = pointer;
			
		}
		// following line is a placeholder for compilation
		//return null;
		bi.front = first;
		return bi;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		
		BigInteger bigint = new BigInteger(); //this is the return value
		
		if ((first.front==null) && (second.front==null)) { //if zeros or empty, return empty big int list
			return bigint;
		}
		
		DigitNode firstllptr = first.front;	//linked list pointers for "first" and "second"
		DigitNode secondllptr = second.front;
		
		DigitNode newllfrontptr = null; //linked list pointers for output BigInteger linked list
		DigitNode newlllastptr = null;
		
		int carryone = 0; //for adding 
		int subtract = 0; //for subtracting
				
	
		// If exactly one of the integers are negative, subtract instead		
		if ((first.negative==true)^(second.negative==true)) {
			
			//if the number with more digits is negative, so is the answer
			if ((first.negative==true)&& (first.numDigits>second.numDigits)) {
				bigint.negative=true;
			}
			if ((second.negative==true)&& (second.numDigits>first.numDigits)) {
				bigint.negative=true;
			}
			
			
			
			int p; //variable for new node digit value
			
			// switches variables if second integer is larger than first
			if (second.numDigits > first.numDigits) {
				firstllptr = second.front;
				secondllptr = first.front;
				bigint.negative = true;
			}
			
			
			//if both lists have the same number of digits
			//need to create two new Big Integer lists which are reversed copies of original big integers
			//this allows us to compare element by element to see which has a larger magnitude
			//this determines whether the answer is negative or positive
			//and whether we need to switch the pointers so the larger number is on top, if subtracting
			
			if (second.numDigits == first.numDigits) { 
				BigInteger firstreverse = first; //copys big int linked lists to reverse, so as not to alter originals
				BigInteger secondreverse = second;
				
				DigitNode firsthead = firstreverse.front;
				DigitNode firstcurrent = firstreverse.front; 
				DigitNode prevfirst = null;
				DigitNode nextfirst = null;
				
				DigitNode secondhead = secondreverse.front;
				DigitNode seccurrent = secondreverse.front;
				DigitNode prevsec = null;
				DigitNode nextsec = null;
				
				
				
				while (firstcurrent != null) { //reverse both lists
					
					nextfirst = firstcurrent.next;
					firstcurrent.next = prevfirst;
					prevfirst = firstcurrent;
					firstcurrent = nextfirst;
					
					nextsec = seccurrent.next;
					seccurrent.next = prevsec;
					prevsec = seccurrent;
					seccurrent = nextsec;
					

				}
				firsthead = prevfirst;
				secondhead = prevsec;
				
				DigitNode firstptr = firsthead;
				DigitNode secptr = secondhead;
				
				while ((firstptr.digit==secptr.digit) && (firstptr.next!=null) && (secptr.next!=null)) { //traversing lists, looking for first difference in integers
					firstptr = firstptr.next;
					secptr = secptr.next;
				}
				
				
				if ((firstptr==null) || (secptr==null)) { //same exact numbers. One is negative so it will return 0, empty big int
					return bigint;
				} 
				
				if (firstptr.digit>secptr.digit){ //first biginteger has a larger magnitude keep order as is. 
					if (first.negative == true) {
						bigint.negative = true;
					}
					if (second.negative == true) {
						bigint.negative = false;
					}
				}
				
				if (firstptr.digit<secptr.digit){ //second big integer has a larger magnitude, switch order
					firstllptr = second.front;
					secondllptr = first.front;
					if (second.negative == true) {
						bigint.negative = true;
					}
					if (first.negative == true) {
						bigint.negative = false;
					}
					
				}
				
				
			}
			
			
			
			while (firstllptr !=null) {
				
				if (secondllptr==null) {
					p = firstllptr.digit + subtract;
					subtract = 0;
					
					DigitNode nd = new DigitNode(p, null);
					
					if ((p==0) && (firstllptr.next==null)){ //dont bring down zero if its the last digit (ie front)
						break;
					}
					
					if (newllfrontptr == null) { //if first node
						newllfrontptr = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					else { //otherwise, connect it to last node
						newlllastptr.next = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					
					firstllptr = firstllptr.next;
					continue;
				
				}
				
				
				if (firstllptr.digit+subtract >= secondllptr.digit) {
					p = firstllptr.digit+subtract - secondllptr.digit;
					subtract = 0;
					
					DigitNode nd = new DigitNode(p, null);
					
					if (newllfrontptr == null) { //if first node
						newllfrontptr = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					else { //otherwise, connect it to last node
						newlllastptr.next = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					
					
				}
				
				if (firstllptr.digit+subtract < secondllptr.digit) {
					p = ((firstllptr.digit+10+subtract) - secondllptr.digit);
					subtract = -1;
					
					DigitNode nd = new DigitNode(p, null);
					
					if (newllfrontptr == null) { //if first node
						newllfrontptr = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					else { //otherwise, connect it to last node
						newlllastptr.next = nd;
						newlllastptr = nd;
						bigint.numDigits++;
					}
					
				
				}
				
				
				
				firstllptr = firstllptr.next;
				if (secondllptr!=null) {
					secondllptr = secondllptr.next;
				}
			}
						
			
		}  //end of "if exactly one is negative" ie subtraction
		
		
		
		
		
		
		else {
		
		//SUM: loop until one of the linked lists reaches null (the end)
		
		while ((firstllptr != null) && (secondllptr != null)) {
			int n = firstllptr.digit + secondllptr.digit + carryone;
			carryone = 0;
			
			
			//carry the one unless adding the very last digit from both lists
			if (n>=10) {
				carryone = 1;
			}
			
			
			if ((n>=10) &&  ((firstllptr.next!=null) || (secondllptr.next!=null))){ 
				n = n - 10;
			}
			
			if ((n>=10) &&  ((firstllptr.next==null) && (secondllptr.next==null))){ //make sure double digits are not parsed as one integer
				int x = n%10;
				int z = (n-x)/10;
				
				
				// splits up double digits into separate nodes
				DigitNode newdn = new DigitNode(x, null); 
				if (newllfrontptr == null) {
					newllfrontptr = newdn;
					newlllastptr = newdn;
					bigint.numDigits++;
				}
				else {
					newlllastptr.next = newdn;
					newlllastptr = newdn;
					bigint.numDigits++;
				}	
					
				//advances pointers
				firstllptr = firstllptr.next;
				secondllptr = secondllptr.next;
				
				// splits up double digits into separate nodes
				DigitNode newdz = new DigitNode(z, null); 
					newlllastptr.next = newdz;
					newlllastptr = newdz;
					bigint.numDigits++;

				
				break;
			}
		
		// creates and links new node with summed integer
		DigitNode newdn = new DigitNode(n, null); 
		if (newllfrontptr == null) {
			newllfrontptr = newdn;
			newlllastptr = newdn;
			bigint.numDigits++;
		}
		else {
			newlllastptr.next = newdn;
			newlllastptr = newdn;
			bigint.numDigits++;
		}	
			
		//advances pointers
		firstllptr = firstllptr.next;
		secondllptr = secondllptr.next;
			
		
		}
		
		
		// these take care of the case where one big integer has more digits than the other
		//first list is shorter
		while ((firstllptr == null) && (secondllptr != null)) {
			
			int n = secondllptr.digit + carryone;
			carryone = 0;
			
			if ((n>=10) &&  (secondllptr.next!=null)){ 
				n = n - 10;
				carryone = 1;
			}
			
			
			DigitNode newdn = new DigitNode(n, null); 
			if (newllfrontptr == null) {
				newllfrontptr = newdn;
				newlllastptr = newdn;
				bigint.numDigits++;
			}
			else {
				newlllastptr.next = newdn;
				newlllastptr = newdn;
				bigint.numDigits++;
			}	
			
			secondllptr = secondllptr.next;
			
		}
		
		//second list is shorter
		while ((firstllptr != null) && (secondllptr == null)) {
			int n = firstllptr.digit + carryone;
			carryone = 0;
			
			if ((n>=10) &&  (firstllptr.next!=null)){ 
				n = n - 10;
				carryone = 1;
			}
			
			DigitNode newdn = new DigitNode(n, null); 
			if (newllfrontptr == null) {
				newllfrontptr = newdn;
				newlllastptr = newdn;
				bigint.numDigits++;
			}
			else {
				newlllastptr.next = newdn;
				newlllastptr = newdn;
				bigint.numDigits++;
			}
			firstllptr = firstllptr.next;
		
		}
		
		} //end of addition
		
		//creates new BigInteger object 
		// and assigns it's front attribute to point to the new linked list front pointer
		
		bigint.front = newllfrontptr;
		
		
		if ((first.negative==true) && (second.negative==true)) {
			bigint.negative = true;
		}
		
		
		
		int countzeros = 0;
		int countalldigits = 0;
		

		if ((bigint.front.digit == 0)) { //if the front is 0, make sure the whole integer is not a string of zeros
			
			
			for (DigitNode current = newllfrontptr; current!=null; current = current.next) {
				
				countalldigits++;
				
				if (current.digit == 0) {
					countzeros++;
					
				}
			}	

		if (countalldigits == countzeros) { //if all digits are zero, make it a single node with one 0
			bigint.front = new DigitNode(0,null);
			return bigint;
		}
		
		}
		
		return bigint;
		
			
}
		
		
		
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		
		int carry = 0; 
		int count = -1; //keeps count to appropriate place holding zeros where neccesssary
		BigInteger bil = new BigInteger(); //makes big integer linked list for every multiplied line which needs to be added later
		BigInteger tst = new BigInteger(); //makes big int linked list for summed up values. this is what is returned later

		BigInteger second2 = new BigInteger();
		BigInteger first1 = new BigInteger();
		
		//swaps variables if first list is shorter than the second
		//this ensures that the larger for loop is always the integer with the least number of digits
		if (first.numDigits>=second.numDigits) {
			second2 = second;
			first1 = first;
			
		}
		if (first.numDigits<second.numDigits) {
			second2 = first;
			first1 = second;
			
		}
		
			for (DigitNode current2 = second2.front; current2!=null; current2 = current2.next) {

				DigitNode newllfrontptr = null;
				DigitNode newlllastptr = null;
				carry = 0;
				count++;
				
				
				
				if (count>=1) {
				DigitNode zero = new DigitNode(0, null);
				bil.numDigits++;
				newllfrontptr = zero;
				newlllastptr = zero;

				
				if (count>1) {
				for (int i = 1; i<count; i++) {
					bil.numDigits++;
					DigitNode zo = new DigitNode(0,null);
					newlllastptr.next = zo;
					newlllastptr = zo;
						}
					}
				
				}
				
				
				
				
				for (DigitNode current1 = first1.front; current1!=null; current1 = current1.next) {
					
					
					int n = (current2.digit * current1.digit) + carry;
					
					
					
					
					int m = n%10;


					DigitNode dn = new DigitNode(m, null);
					carry = (n-m)/10;
					
					
					
					
					if (newllfrontptr == null) {
						bil.numDigits++;
						newllfrontptr = dn;
						newlllastptr = dn;
					}
					else {
						bil.numDigits++;
						newlllastptr.next = dn;
						newlllastptr = dn;
					}
					
					
					
				//	bil.front = newllfrontptr;	
					
		
					
					
				if ((current1.next==null) && (carry!=0))	{ //reached the end of the row, dont carry last digit
					DigitNode dnc = new DigitNode(carry, null);
					dnc.digit = carry; //make sure only one integer per node fix //////////
						bil.numDigits++;
						newlllastptr.next = dnc;
						newlllastptr = dnc;
				}

					
					bil.front = newllfrontptr;	
				

					
	
				
				
				
				} 
				

				tst = add(bil, tst); //adds each row cumulatively to a new Big Integer
		
				

				} //inner loop
			
			//if exactly one of the original big ints is negative, so is the answer
			if ((first.negative == true) ^ (second.negative == true)) {
			tst.negative = true;
			}
			
			
			// following line is a placeholder for compilation
				return tst;
			
			}//end of method
					


			


	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
