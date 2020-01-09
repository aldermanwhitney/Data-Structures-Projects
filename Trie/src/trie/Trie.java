package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		
		//this root node will remain empty, 
		// eventually only have children, no siblings or indexes
		TrieNode root = new TrieNode(null, null, null); 
		TrieNode rootptr = root; //root pointer
		TrieNode ptr =  null; //traverses list 
		TrieNode largestmatchingprefix = null; //saves node with largest match
		TrieNode lastsib = null;
		TrieNode prev = null;
		TrieNode oldfirstchild = null;

		boolean match = false; //used to match prefixes
		
		int e = 0; //used to compare letters
		int countlargest = 0; //entire word letter match count
		int letterssharedatcurrentnode = 0; //num of letters which match at current node
	
		boolean firstchild = false;
		
		for (int i=0; i<allWords.length; i++) { //iterate through allWords array, word by word

		System.out.println("Current Word to Insert: " + allWords[i]);	
		
			// if there are not yet any nodes in the tree (besides the root)
			// create new node for this word, and it will be the child of the root
			if (root.firstChild==null) {
				Indexes firstind = new Indexes(0, (short)0, (short)(allWords[i].length()-1));
				TrieNode first = new TrieNode(firstind, null, null);
				root.firstChild = first;
				lastsib = first;
				continue;
			}
			
				// TWO POINTERS NEEDED - PREV AND CURR
				//traverse: first child (if entire match found) -> siblings (need to check each sibling indv)
					// find largest match: (update by overwriting if better match found)
					// if the whole word is a match again, go to first child at this node and traverse through -> siblings
					// looking for closest match
					// stop traversing when: prev holds a match but current has not found a closer match OR firstchild==null
				//after traversing, when largest match has been found:
				// if the largest match is part of the word, split this node (see code from before)
				// if the largest match is the whole "word" - this wont exist
		
			ptr = root.firstChild;
			
			while (ptr!=null) {

				
					// if the letter in the word to be inserted and the corresponding letter in the current node match
					// continue iterating through the word to be inserted and the current node to see how much of it matches
				if (ptr.substr.endIndex>=e) {
					
					// make sure e does not exceed number of characters at the node
					//if it does, that means there is a better match somewhere else anyway, can skip this node
					while (allWords[i].charAt(e)==allWords[ptr.substr.wordIndex].charAt(e)) {
						
						
						System.out.println("(word to be inserted) Char at e: " + allWords[i].charAt(e));
						System.out.println("(current node word) Char at e: " + allWords[ptr.substr.wordIndex].charAt(e));
						
						
						
						match = true;
						letterssharedatcurrentnode++;
						countlargest++;
						largestmatchingprefix = ptr; 
						
						//if the entire words are equal, check first child and iterate through there for closer matches
						// if the last letter in the current node matches, traverse downward into firstchild
						
						//if matching portion of word consists of the entire node, traverse downward into firstchild
						//if (allWords[ptr.substr.wordIndex].length()-1==e) {
						if (ptr.substr.endIndex==e) {	
							if (ptr.firstChild!=null) {
								
							ptr = ptr.firstChild; //traverse downward
							
							System.out.println("traverse down to first child, ptr at: " + allWords[ptr.substr.wordIndex]);
							
							letterssharedatcurrentnode = 0; //new node, reset counter
							e++;
							firstchild=true;
							break; // break out to traverse siblings
							}
						}	
						e++;
						System.out.println("e: " + e);
						//if the word to be inserted has been iterated to the end
						if (allWords[i].length()<=e || allWords[ptr.substr.wordIndex].length()<=e) { 
							break;
						}
						
						
					}	
					}	
					if (firstchild==true){ //if we are on the first child, need to check this node before traversal
						firstchild = false;
						continue;
					}
				lastsib = ptr; //keep track of last sibling in case of no matches
				ptr = ptr.sibling; //no match found, search siblings
				if (ptr!=null) {
				System.out.println("pointer at: " + allWords[ptr.substr.wordIndex]);
				}
				else {
					System.out.println("pointer is null");
				}
				} //by this point our "largest matching prefix" pointer should point to the largest matching prefix in the tree
				
				if (largestmatchingprefix!=null){
				System.out.println("Largest Matching Prefix: word index - " + largestmatchingprefix.substr.wordIndex);
				System.out.println("Largest Matching Prefix: start index - " + largestmatchingprefix.substr.startIndex);
				System.out.println("Largest Matching Prefix: end index - " + largestmatchingprefix.substr.endIndex);
				}
			
				
				//if only part of the prefix matches - done iterating
				// ifthe whole prefix matches, go to first child and check for further matches
				
				
				
				// found largest match
				if (largestmatchingprefix!=null) {
					
				//TWO CASES - FULL MATCH OR PARTIAL MATCH AT NODE
			
					
					
					//CASE 1 - full match at a node, no matches in children
					//iterate though children of match to last sibling
					//place remaning part of word at the last sibling.sibling
					//if there is a match and the letters shared at current node are zero, largestmatchingprefix is a whole match
					if (letterssharedatcurrentnode == 0) {
						
						System.out.println("HERE");
						ptr = largestmatchingprefix.firstChild;
						
						//iterate to very last sibling, place new word node here
						while (ptr!=null) {
							prev = ptr;
							System.out.println("Pointer: " + ptr.substr);
							ptr = ptr.sibling;
							
						}
						
						Indexes fmi = new Indexes(i, (short)countlargest, (short)(allWords[i].length()-1));
						TrieNode fm = new TrieNode(fmi, null, null);
						prev.sibling = fm;						
					}
					
					
					//CASE 2 - partial match at node
					if (letterssharedatcurrentnode > 0) {
						
						//save old info at old lmp node
						int wilm = largestmatchingprefix.substr.wordIndex;
						int startlm = largestmatchingprefix.substr.startIndex;
						int endlm = largestmatchingprefix.substr.endIndex;
						
						if (largestmatchingprefix.firstChild!=null) {
							//save old first child
							int wifc = largestmatchingprefix.firstChild.substr.wordIndex;
							int startfc = largestmatchingprefix.firstChild.substr.startIndex;
							int endfc = largestmatchingprefix.firstChild.substr.endIndex;
							
							
							oldfirstchild = largestmatchingprefix.firstChild;
						}
						
						
						// change largestmp node to new match
						largestmatchingprefix.substr.endIndex = (short)(countlargest-1);
						
						
						
						// new node for remaining letters of word we are inserting
						Indexes newwordinsertind = new Indexes(i, (short)(countlargest), (short)(allWords[i].length()-1));
						TrieNode newwordinsert = new TrieNode(newwordinsertind, null, null);
						
						
						//make new first child of lmp part we took off of old node
						//Indexes lmpnfci = new Indexes(wilm, (short)(largestmatchingprefix.substr.endIndex + 1), (short)(allWords[wilm].length()-1));
						Indexes lmpnfci = new Indexes(wilm, (short)(largestmatchingprefix.substr.endIndex + 1), (short)(endlm));
						
						
						
						//this new node is the new first child of the largest matching prefix
						//make first child of this node the old first child
						//make the sibling of this node the rest of the word we are inserting 
						TrieNode lmpnfc = new TrieNode(lmpnfci, oldfirstchild, newwordinsert);
						
						largestmatchingprefix.firstChild = lmpnfc;
						
						
					}
					
						
	

		} 
				
				if (largestmatchingprefix==null) { //no matches
					
					//create new node and attach to last sibling
					Indexes lsindex = new Indexes(i, (short)0, (short)(allWords[i].length()-1));
					TrieNode lastsibling = new TrieNode(lsindex, null, null);
					lastsib.sibling=lastsibling;
					
					
					
				}
				
				//reset variables for next iteration
				largestmatchingprefix = null;
				lastsib = null;

				match = false; //used to match prefixes
				ptr =  null;
				
			
				countlargest = 0;
				e=0;
			
				firstchild = false;		
				
				oldfirstchild = null;
				
				
		} //end of largest while loop - iterating through array of words to insert
		return root;	
	} // end of method
		
		

		
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<TrieNode> arrlist = new ArrayList<>();
		
	
		TrieNode ptr = root;
		
		int si;
		int ei;
		
		if (ptr==null) { //base case
			System.out.println("before returning null");
			return null; //empty trie
			
		}
		
		
		
		if (ptr.sibling==null && ptr.substr==null) { //only the root will have both these as null
			ptr = ptr.firstChild;
		}
				
		int prefixlength = prefix.length();
		
		
		
		while (ptr!=null) {
			
		int nodelength = (ptr.substr.endIndex - ptr.substr.startIndex)+1;	
		


		
		// if the pointer node's word equals the prefix string
		// or the first chars match and the prefix and smaller than the ptr
		// add all leaf nodes below it
		
		if (allWords[ptr.substr.wordIndex].length()>=prefixlength) {

			if (((allWords[ptr.substr.wordIndex].substring(0,1)).equals(((prefix).substring(0,1))))){
			
			//if equal, add all leaf nodes below
		
			System.out.println("first letters match");
			si = ptr.substr.startIndex;
			ei = ptr.substr.endIndex + 1;
			System.out.println(" ptr at: " + allWords[ptr.substr.wordIndex].substring(si, ei));
			
			if (ptr.firstChild==null && (allWords[ptr.substr.wordIndex].substring(0,prefixlength)).equals(((prefix).substring(0,prefixlength)))) {
				System.out.println("leaf node");	
				arrlist.add(ptr);
				si = ptr.substr.startIndex;
				ei = ptr.substr.endIndex + 1;
				System.out.println(" ptr at: " + allWords[ptr.substr.wordIndex].substring(si, ei));
	
			}
	
			else if (ptr.firstChild!=null && (allWords[ptr.substr.wordIndex].substring(0,prefixlength)).equals(((prefix).substring(0,prefixlength)))) {
				System.out.println("NOT leaf node");
				arrlist.addAll(0,completionList(ptr.firstChild, allWords, prefix));
				si = ptr.substr.startIndex;
				ei = ptr.substr.endIndex + 1;
				System.out.println(" ptr at: " + allWords[ptr.substr.wordIndex].substring(si, ei));
			

			}
			
			
			else if (ptr.firstChild!=null && (allWords[ptr.substr.wordIndex].substring(0,ei)).equals(((prefix).substring(0,ei)))) {
				System.out.println("NOT leaf node, does not equal prefix yet");
				System.out.println(" ptr at: " + allWords[ptr.substr.wordIndex].substring(si, ei));
				//completionList(ptr.firstChild, allWords, prefix);
				ptr = ptr.firstChild;
				si = ptr.substr.startIndex;
				ei = ptr.substr.endIndex + 1;
				System.out.println(" ptr at: " + allWords[ptr.substr.wordIndex].substring(si, ei));
			} 
				
			else {
				
				//completionList(ptr.sibling, allWords, prefix);
			}
			
			
			
			ptr = ptr.sibling;
			
		}
		else {
			ptr=ptr.sibling;
			continue;
		}
		}
		else {
			ptr = ptr.sibling;
		}
		
		}
		
		if (arrlist.isEmpty()) {
			return null;
		}
		return arrlist;	
		
		
		}
		
		

	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
