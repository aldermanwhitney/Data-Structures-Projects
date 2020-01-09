package friends;

public class Test {
    public static ArrayList<String> connectors(Graph g) {

        //initalize
        boolean[] visited = new boolean[g.members.length]; //all set to false automatically
        int[] dfsnum = new int[g.members.length];
        int[] back = new int[g.members.length];
        ArrayList<String> answer = new ArrayList<>();

        //driver
        for (Person member : g.members) {
            //if it hasn't been visited
            if (!visited[g.map.get(member.name)]){
                //reset dfsnum for different islands and dfs call
                dfsnum = new int[g.members.length];
                dfs(g.map.get(member.name), g.map.get(member.name), g, visited, dfsnum, back, answer);
            }
        }

        //error checking below:

        //check if only have one edge, then cannot be a connector
        for (int i = 0; i < answer.size(); i++) {
            Friend ptr = g.members[g.map.get(answer.get(i))].first;

            int count = 0;
            while (ptr != null) {
                ptr = ptr.next;
                count++;
            }

            //if no edge or only one edge
            if (count == 0 || count == 1) {
                answer.remove(i);
            }
        }


        //check if something has only one neighbor, then neighbor must be connector
        for (Person member : g.members) {
            if ((member.first.next == null && !answer.contains(g.members[member.first.fnum].name))) {
                answer.add(g.members[member.first.fnum].name);
            }
        }

        return answer;
    }

    //find size of dfsnum because counting up and passing it back in recursion does not work
    //to assign the dfsnum and back number
    private static int sizeArr(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                count++;
            }
        }
        return count;
    }

    //recursive dfs
    private static void dfs(int v, int start, Graph g, boolean[] visited, int[] dfsnum, int[] back, ArrayList<String> answer){
        //get the person and mark as visited
        Person p = g.members[v];
        visited[g.map.get(p.name)] = true;
        int count = sizeArr(dfsnum)+1;

        //if not set, set it
        if (dfsnum[v] == 0 && back[v] == 0) {
            dfsnum[v] = count;
            back[v] = dfsnum[v];
        }

        //go through neighbors
        for (Friend ptr = p.first; ptr != null; ptr = ptr.next) {
            //if not visited
            if (!visited[ptr.fnum]) {

                //recursive call on neighbors
                dfs(ptr.fnum, start, g, visited, dfsnum, back, answer);

                //after come back, check
                if (dfsnum[v] > back[ptr.fnum]) {
                    //just change the back[v] num
                    back[v] = Math.min(back[v], back[ptr.fnum]);
                } else {
                    //trying to fix if starting point has two edges but not a connector and other issues fixed with this
                    if (Math.abs(dfsnum[v]-back[ptr.fnum]) < 1 && Math.abs(dfsnum[v]-dfsnum[ptr.fnum]) <=1 && back[ptr.fnum] ==1 && v == start) {
                        //don't add if both 1's
                        continue;
                    }

                    //if these conditions, IS A CONNECTOR
                    if (dfsnum[v] <= back[ptr.fnum] && (v != start || back[ptr.fnum] == 1 )) { //not the startng point
                        if (!answer.contains(g.members[v].name)) {
                            //if not already in there, add the connector to the list
                            answer.add(g.members[v].name);
                        }
                    }

                }
            } else {
                //if already visited, update back[v]
                back[v] = Math.min(back[v], dfsnum[ptr.fnum]);
            }
        }
        return;
    }

}
}
