import java.util.*;
import java.io.*;

public class maxflow {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };
	final static int MAX_N = 100000;
	final static int MAX_D = 17;
	
	static int N;
	static int[][] lca = new int[MAX_N][MAX_D];
	static int[] depth = new int[MAX_N]; 
	static ArrayList<Integer>[] adj;
	static int[] pref;
	static int[] parents;
	static int max;
	
	public static void main(String[] args) throws Exception {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("maxflow.in"));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		PrintWriter out = new PrintWriter(new File("maxflow.out"));
		N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		adj = new ArrayList[N];
		pref = new int[N];
		parents = new int[N];
		parents[0] = -1;
		max = 0;
		for(int i = 0; i != N; ++i) {
			adj[i] = new ArrayList<Integer>();
		}
		for(int i = 0; i != N - 1; ++i) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			adj[x].add(y); adj[y].add(x);
		}
		dfssetup(0, -1);
		initLCA();
		for(int i = 0; i != K; ++i) {
			st = new StringTokenizer(br.readLine());
			int s = Integer.parseInt(st.nextToken()) - 1;
			int t = Integer.parseInt(st.nextToken()) - 1;
			int LCA = LCA(s,t);
			++pref[s];
			++pref[t];
			--pref[LCA];
			if(parents[LCA] != -1) --pref[parents[LCA]];
		}
		int[][] revOrder = new int[N][2];
		for(int i = 0; i != N; ++i) {
			revOrder[i][0] = i;
			revOrder[i][1] = i;
		}
		Arrays.sort(revOrder, (a, b) -> depth[b[0]] - depth[a[0]]);
		for(int i = 0; i != N; ++i) {
			if(parents[revOrder[i][0]] == -1) continue;
			pref[parents[revOrder[i][0]]] += pref[revOrder[i][0]];
		}
		int max = 0;
		for(int i = 0; i != N; ++i) {
			max = Math.max(max, pref[i]);
		}
		out.println(max);
		br.close();	
		out.close();
	}
	static void dfssetup(int curr, int par) {
		for(int out: adj[curr]) {
			if(out == par)
				continue;
			depth[out] = depth[curr] + 1;
			lca[out][0] = curr;
			parents[out] = curr;
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