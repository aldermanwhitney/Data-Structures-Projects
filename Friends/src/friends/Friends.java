package friends;

import java.util.ArrayList;
import java.util.Collections;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// BFS SEARCH
		// prev nodes stored in array for backtracing to find path once destination is found
		// p1 is starting vertex, p2 is ending vertex
		
		
		System.out.println("in Friends: shortestChain method.");

		
		ArrayList<String> result = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();
		boolean[] visited = new boolean[g.members.length]; //T/F array for whether node has been visited
	
		// An array which stores the index of the parent node for back tracing
		// ie array[i] = Person i's parent index
		int[] prevnode = new int[g.members.length];
		
		boolean finished = false;
		int p1index = 0;
		
		//visit first vertex - p1
		
		// Make sure its case insensitive - not guaranteed file will be all lower or uppercase
		if(g.map.containsKey(p1.toLowerCase())){
			  p1index = g.map.get(p1.toLowerCase());
			}
		else if(g.map.containsKey(p1.toUpperCase())){
			  p1index = g.map.get(p1.toUpperCase());
			}
		else if(g.map.containsKey(p1)){
			p1index = g.map.get(p1); //get index of person p1
		}
		else {
			return null;
		}
		visited[p1index]=true; //set boolean visited to true
		
		System.out.println("Visited person 1, " + p1);
		q.enqueue(g.members[p1index]); //Add person 1 to queue
		
		while (!q.isEmpty()) {
			
			if (finished==true) {
				break;
			}
			
			String p = q.dequeue().name;
			int v = g.map.get(p); //dequeue next person and get their index number
			
			for (Friend ptr = g.members[v].first; ptr!=null; ptr=ptr.next) {
				int fnum = ptr.fnum; //get current nodes fnum
				
				if (!visited[fnum]) { //if a friend is not visited
					System.out.println("Visiting " + g.members[fnum].name);
					q.enqueue(g.members[fnum]);
					visited[fnum]=true;
					
					prevnode[fnum] = v; //saves link to parent node in array for back tracing
					
					if (g.members[fnum].name.equalsIgnoreCase(p2)){ //we have reached person 2
						result.add(g.members[fnum].name); //add last person to arraylist path
						
						//backtracking - while we have not yet reached person 1
						while (!g.members[fnum].name.equalsIgnoreCase(p1)) { 
							fnum = prevnode[fnum]; //trace path through parent nodes
							result.add(g.members[fnum].name);
							System.out.println("Added: " + g.members[fnum].name);
						}
							finished = true;
							break;
						
					}
					
					
				}
					
			}
			
		}
				
		System.out.println("Arraylist size: " + result.size());
		Collections.reverse(result);
		System.out.println(result);
		if (result.size()==0) {
			System.out.println("Returning null");
			return null;
		}
		return result;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/

		System.out.println("in Friends: cliques method.");
		
		//DRIVER
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		
		//iterate through boolean array and visit unvisited students of correct school type
		for (int i=0; i<visited.length; i++) {
			if (!g.members[i].student) { //avoid null ptr exc. if not a student
				continue;
			}
			
			//Found unvisited value in array which matches type we are looking for
			// Had to use driver to restart, so must be new clique
			if (!visited[i] && g.members[i].school.equalsIgnoreCase(school)) {
				System.out.println("Had to restart driver, must be new clique");
				ArrayList<String> newclique = new ArrayList<String>();
				DFSrec(i, visited, g, school, newclique);
				
				result.add(newclique); //add new clique to ultimate list of cliques
			}
		}
		
        for(ArrayList<String> str : result) {
            System.out.println(str);
        }
        
        
        System.out.println(result.size());
        if (result.size()==0) {
        	System.out.println("Returning Null");
        	return null;
        }
        
		return result;
		
	}
	
	
	private static void DFSrec(int v, boolean[] visited, Graph g, String school, ArrayList<String> newclique){
		
		visited[v]=true;
		System.out.println("Visiting " + g.members[v].name);
		newclique.add(g.members[v].name); //add matching students to current clique
		
        for(String str : newclique) { //print statement for debugging
            System.out.println(str);
        }
		
		for(Friend ptr = g.members[v].first; ptr!=null; ptr=ptr.next) {
			if (!g.members[ptr.fnum].student) { //avoid null ptr exc. if not a student
				continue;
			}
			
			//Found friend who matches school type, thus same clique
			//Continue DFS, adding names until null or different school
			if (!visited[ptr.fnum] && g.members[ptr.fnum].school.equalsIgnoreCase(school)) {
				System.out.println("(" + g.members[ptr.fnum].name + " not yet visited)");
				DFSrec(ptr.fnum, visited, g, school, newclique);
			}
		}
		
		
	}
	
	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		//DRIVER
		System.out.println("in Friends: connectors method.");
		
		
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		//int count = 1;
		ArrayList<String> result = new ArrayList<String>();
		Queue<Integer> count = new Queue<Integer>();
		
		// Fill up queue with counts to be used later in dfssnum
		for (int j=1; j<=g.members.length+10; j++) {
			count.enqueue(j);
		}
		
		boolean[] visited = new boolean[g.members.length];
		
		for (int i=0; i<visited.length; i++) {
			if (!visited[i]) {
				recDFSConnectors(i, visited, g, dfsnum, back, count, result);
			}
		}
		
		System.out.println("dfs num:");
		for (int a=0; a<dfsnum.length; a++) {
		System.out.print(dfsnum[a] + "  ");
		}
		
		System.out.println("");
		System.out.println("back:");
		for (int b=0; b<back.length; b++) {
		System.out.print(back[b] + "  ");
		}
		
		System.out.println("");
		System.out.println("ArrayList result");
		for (int c=0; c<result.size(); c++) {
		System.out.print(result.get(c) + "  ");
		}
		
		
        //System.out.println(result.size());
        if (result.size()==0) {
        	System.out.println("Returning Null");
        	return null;
        }
		return result;
		
		
	}
	
	
	private static void recDFSConnectors(int v, boolean[] visited, Graph g, int[] dfsnum, int[] back, Queue<Integer> count, ArrayList<String> result) {
		
		visited[v]=true;
		System.out.println("Visited " + g.members[v].name);
		
		dfsnum[v]=count.dequeue();
		back[v]=dfsnum[v];
		System.out.println(dfsnum[v]);
		
		
		
		for (Friend ptr = g.members[v].first; ptr!=null; ptr = ptr.next) {
			
			if (!visited[ptr.fnum]) {
				recDFSConnectors(ptr.fnum, visited, g, dfsnum, back, count, result);
				//When the DFS backs up from a neighbor, w, to v, 
				//if dfsnum[v] > back(w), then back(v) is set to min(back(v),back(w))
				//If a neighbor, w, is already visited then back(v) is set to min(back(v),dfsnum(w))
				
				if (dfsnum[v]>back[ptr.fnum]) {
					back[v]=Math.min(back[v], back[ptr.fnum]);
				}

			}
			//If a neighbor, w, is already visited then back(v) is set to min(back(v),dfsnum(w))
			back[v]=Math.min(back[v], dfsnum[ptr.fnum]);
			
		
			//When the DFS backs up from a neighbor, w, to v, if dfsnum(v) <= back(w), 
			//then v is identified as a connector, 
			//IF v is NOT the starting point for the DFS.
			//NOT starting point
			if (dfsnum[v]<=back[ptr.fnum] && !result.contains(g.members[v].name) && (v!=0)){ // || back[ptr.fnum]==1)){
				result.add(g.members[v].name);
			}
			//////////////////////////////////
			//If deleted - starting point can never be connector.
			//IS starting point:
			//if v does NOT have any neighbors which are non connectors - v is a connector 
			// v has a neighbor that is not on the connector list - w NOT connector 
			if (dfsnum[v]<=back[ptr.fnum] && !result.contains(g.members[v].name) && (v==0)){
				
				//Not a connector, do not add 
                if (Math.abs(dfsnum[v]-dfsnum[ptr.fnum])<=1 && Math.abs(dfsnum[v]-back[ptr.fnum])<1) {
				continue;
                }
                
				for (Friend nbr = g.members[v].first; nbr!=null; nbr = nbr.next){
				if(!result.contains(g.members[nbr.fnum].name)){ //if neighbors are not connectors
					System.out.println("CONNECTOR");
					if (!result.contains(g.members[v].name)){
					result.add(g.members[v].name);
					}

				} 
				else {
					System.out.println("NOT A CONNECTOR");
					if (!result.contains(g.members[v].name)) {
					
					}
				}
				}
				
			}
			////////////////////////////////
			
		}
		
		
	}
	
	
	
	
	
}

