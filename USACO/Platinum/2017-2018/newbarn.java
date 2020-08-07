import java.util.*;
import java.io.*;

public class newbarn {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };
	static ArrayList<Integer>[] adj;
	static int[] depth, ids;
	static int[][] diam;
	static int numTrees, numNodes;
	static void dfs(int v, int p, int id) {
		ids[v] = id;
		for(Integer i : adj[v]) {
			if(i == p) continue;
			depth[i] = depth[v] + 1;
			dfs(i, v, id);
		}
	}
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("newbarn.in"));
		PrintWriter out = new PrintWriter(new File("newbarn.out"));
		int Q = Integer.parseInt(br.readLine());
		query[] queries = new query[Q];
		numNodes = 0;
		ArrayList<Integer> roots = new ArrayList<Integer>();
		for(int i = 0; i < Q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			char tp = st.nextToken().charAt(0);
			int loc = Integer.parseInt(st.nextToken()) - 1;
			queries[i] = new query(tp, loc);
			if(tp == 'B') {
				++numNodes;
			}
		}
		adj = new ArrayList[numNodes];
		for(int i = 0; i < numNodes; ++i) {
			adj[i] = new ArrayList<Integer>();
		}
		int nodenum = 0;
		for(int i = 0; i < Q; ++i) {
			int loc = queries[i].loc;
			if(queries[i].tp == 0) {
				if(loc >= 0) {
					adj[loc].add(nodenum);
					adj[nodenum].add(loc);
				}
				else {
					roots.add(nodenum);
				}
				++nodenum;
			}
		}
		numTrees = roots.size();
		depth = new int[numNodes];
		ids = new int[numNodes];
		diam = new int[numTrees][2]; //mx depth end, lca end
		int id = 0;
		for(Integer i : roots) {
			dfs(i, -1, id);
			dfssetup(i, -1);
			++id;
		}
		initLCA();
		int numBarn = 0;
		for(int i = 0; i < Q; ++i) {
			if(queries[i].loc == -2) {
				diam[ids[numBarn]][0] = numBarn;
				diam[ids[numBarn]][1] = numBarn;
				++numBarn; 
				continue;
			}
			int par = queries[i].loc;
			id = ids[par];
			if(queries[i].tp == 0) {
				if(dist(diam[id][0], numBarn) > dist(diam[id][0], diam[id][1])) {
					diam[id][1] = numBarn;
				}
				if(dist(diam[id][1], numBarn) > dist(diam[id][0], diam[id][1])) {
					diam[id][0] = numBarn;
				}
				++numBarn;
			}
			else {
				out.println(Math.max(dist(par, diam[id][0]), dist(par, diam[id][1])));
			}
		}
		br.close();
		out.close();
	}
	static int dist(int a, int b) {
		int lca = LCA(a, b);
		return depth[a] - depth[lca] + depth[b] - depth[lca];
	}
	static class query{
		int tp, loc; //tp 0 is B, 1 is Q
		query(char c, int l){
			if(c == 'B') tp = 0;
			else tp = 1;
			loc = l;
		}
	}
	static int MAX_D = 17;
	static int MAX_N = 100000;
	static int[][] lca = new int[MAX_N][MAX_D];
	static void dfssetup(int curr, int par) {
		for(int out: adj[curr]) {
			if(out == par)
				continue;
			lca[out][0] = curr;
			dfssetup(out, curr);
		}
	}
	static int LCA(int a, int b) {
		if(depth[a] < depth[b]) {
			int temp = a;
			a = b;
			b = temp;
		}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(depth[a] - (1<<d) >= depth[b]) {
	  			a = lca[a][d];
	  		}
	  	}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(lca[a][d] != lca[b][d]) {
	  			a = lca[a][d];
	  			b = lca[b][d];
	    	}
	  	}
	  	if(a != b) {
		  	a = lca[a][0];
	    	b = lca[b][0];
	  	}
	  	return a;
	}
	static void initLCA() {
		for(int d = 1; d < MAX_D; d++) {
			for(int i = 0; i < MAX_N; i++) {
				lca[i][d] = lca[lca[i][d-1]][d-1];
			}
		}
	}
}