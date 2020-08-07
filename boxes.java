/*
import java.io.IOException;
import java.util.*;
public class boxes extends Grader {
	
	@Override
  	public static void main(String args[]) throws IOException {
  		N = getN();
  		Q = getQ();
  		for(int i = 0; i < N; ++i){
  			adj[i] = new ArrayList<Integer>();
  		}
		new boxes().run(); 
  	}

	@Override
  	public void addRoad(int a, int b) {
  		adj[a].add(b);
  		adj[b].add(a);
  	}
  	
	@Override
  	public void buildFarms(){
  		dfssetup(0, -1);
  		initLCA();
  		setFarmLocation(0, 1, cy(0));
  		coords[0] = new coord(1, cy(0));
  		rows[0] = 1;
  		dfs(0, -1);
	}
  	
	@Override
  	public void notifyFJ(int a, int b){
  		int lca = LCA(a, b);
  		int mnxa = Math.min(coords[a].x, coords[lca].x);
  		int mxxa = Math.max(coords[a].x, coords[lca].x);
  		int mnya = Math.min(coords[a].y, coords[lca].y);
  		int mxya = Math.max(coords[a].y, coords[lca].y);
  		int mnxb = Math.min(coords[lca].x, coords[b].x);
  		int mxxb = Math.max(coords[lca].x, coords[b].x);
  		int mnyb = Math.min(coords[lca].y, coords[b].y);
  		int mxyb = Math.max(coords[lca].y, coords[b].y);
  		if(lca == a) {
  			addBox(mnxb, mxxb, mnyb, mxyb);
  		}
  		else if(lca == b) {
  			addBox(mnxa, mxxa, mnya, mxya);
  		}
  		else {
  			addBox(mnxa, mxxa, mnya, mxya);
  			addBox(mnxb + 1, mxxb, mnyb, mxyb);
  		}
  	}
	int N, Q;
    int col = 2;
	ArrayList<Integer>[] adj;
	int MAX_D = 17;
	int MAX_N = 100000;
	coord[] coords = new coord[MAX_N];
    int[] rows = new int[MAX_N];
	int[] depth = new int[MAX_N];
	int[] size = new int[MAX_N];
	int[][] lca = new int[MAX_N][MAX_D];
	void dfssetup(int curr, int par) {
		size[curr] = 1;
		for(int out: adj[curr]) {
			if(out == par)
				continue;
			depth[out] = depth[curr] + 1;
			lca[out][0] = curr;
			dfssetup(out, curr);
			size[curr] += size[out];
		}
	}
	int LCA(int a, int b) {
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

	void initLCA() {
		for(int d = 1; d < MAX_D; d++) {
			for(int i = 0; i < MAX_N; i++) {
				lca[i][d] = lca[lca[i][d-1]][d-1];
			}
		}
	}
	
	int cy(int y) {
  		return N - y;
  	}
  	
  	public void dfs(int v, int p) {
  		int totsize = 0;
  		int row = rows[v] + 1;
  		for(Integer i : adj[v]) {
  			if(i == p) continue;
  			totsize += size[i];
  		}
  		for(Integer i : adj[v]) {
  			if(i == p) continue;
  			totsize -= size[i];
  			rows[i] = row + totsize;
  			coords[i] = new coord(col, cy(row + totsize - 1));
  			setFarmLocation(i, col++, cy(row + totsize - 1));
  			dfs(i, v);
  		}
  	}
	
  	class coord{
  		int x, y;
  		coord(int a, int b){
  			x = a; y = b;
  		}
  	}
}
*/